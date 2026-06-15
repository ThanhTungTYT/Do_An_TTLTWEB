package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.GoogleUser;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AuthService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet("/login-google")
public class GoogleLoginServlet extends HttpServlet {

    private static final String CLIENT_ID = "682060554420-qs22m1250tphcaablu0m653jo1s59n7j.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String code = request.getParameter("code");

        if (code == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            JsonObject tokenJson = getTokenJson(code, request);

            GoogleUser ggUser = parseUserFromIdToken(tokenJson.get("id_token").getAsString());

            AuthDao authDao = new AuthDao();
            AuthService authService = new AuthService();

            User user = authDao.findByEmail(ggUser.getEmail());

            if (user == null) {
                user = new User();
                user.setFull_name(ggUser.getName());
                user.setEmail(ggUser.getEmail());
                user.setPhone(null);
                user.setPassword_hash(null);

                authDao.register(user);

                user = authDao.findByEmail(ggUser.getEmail());
            }

            authService.loadUserPermissions(user);

            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login Google thất bại: " + e.getMessage());
        }
    }

    private JsonObject getTokenJson(String code, HttpServletRequest request) throws IOException {
        String redirectUri = getRedirectUri(request);

        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String params = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8.name()) +
                "&client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8.name()) +
                "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8.name()) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name()) +
                "&grant_type=authorization_code";

        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes());

        InputStream is = (conn.getResponseCode() >= 400)
                ? conn.getErrorStream()
                : conn.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        String response = responseBuilder.toString();
        System.out.println("TOKEN RESPONSE: " + response);

        if (response.isEmpty()) {
            throw new RuntimeException("Response rỗng từ Google");
        }

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (!json.has("id_token")) {
            throw new RuntimeException("Không có id_token: " + response);
        }

        return json;
    }
    private GoogleUser parseUserFromIdToken(String idToken) {

        String[] parts = idToken.split("\\.");

        if (parts.length < 2) {
            throw new RuntimeException("id_token không hợp lệ");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

        JsonObject json = JsonParser.parseString(payload).getAsJsonObject();

        GoogleUser user = new GoogleUser();
        user.setId(json.get("sub").getAsString());
        user.setEmail(json.get("email").getAsString());
        user.setName(json.get("name").getAsString());

        return user;
    }

    private String getRedirectUri(HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (scheme == null) {
            scheme = request.getScheme();
        }

        String serverName = request.getHeader("X-Forwarded-Host");
        if (serverName == null) {
            serverName = request.getServerName();
        }

        if (serverName.contains(":")) {
            serverName = serverName.split(":")[0];
        }

        String portHeader = request.getHeader("X-Forwarded-Port");
        int serverPort = (portHeader != null) ? Integer.parseInt(portHeader) : request.getServerPort();

        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (("http".equals(scheme) && serverPort != 80) || ("https".equals(scheme) && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append("/login-google");
        return url.toString();
    }
}