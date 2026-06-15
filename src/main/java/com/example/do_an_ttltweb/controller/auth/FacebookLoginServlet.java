package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.dao.AuthDao;
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

@WebServlet("/login-facebook")
public class FacebookLoginServlet extends HttpServlet {

    private static final String APP_ID = "1989296738622542";
    private static final String APP_SECRET = "da4de0792e7d5ff5df0f9346f9c8a5f5";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String code = request.getParameter("code");

        if (code == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            String accessToken = getAccessToken(code, request);
            JsonObject fbUser = getUserInfo(accessToken);

            String email = fbUser.has("email") ? fbUser.get("email").getAsString() : null;
            String name = fbUser.get("name").getAsString();
            String fbId = fbUser.get("id").getAsString();

            String identifier = (email != null) ? email : "fb_" + fbId + "@facebook.com";

            AuthDao authDao = new AuthDao();
            AuthService authService = new AuthService();

            User user = authDao.findByEmail(identifier);

            if (user == null) {
                user = new User();
                user.setFull_name(name);
                user.setEmail(identifier);
                user.setPhone("");
                user.setPassword_hash(null);

                authDao.register(user);
                user = authDao.findByEmail(identifier);
            }

            if (user != null) {
                authService.loadUserPermissions(user);
            }

            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login Facebook thất bại: " + e.getMessage());
        }
    }

    private String getAccessToken(String code, HttpServletRequest request) throws IOException {
        String redirectUri = getRedirectUri(request);
        System.out.println("DYNAMIC FB REDIRECT URI: " + redirectUri);

        String urlStr = "https://graph.facebook.com/v18.0/oauth/access_token"
                + "?client_id=" + URLEncoder.encode(APP_ID, StandardCharsets.UTF_8.name())
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name())
                + "&client_secret=" + URLEncoder.encode(APP_SECRET, StandardCharsets.UTF_8.name())
                + "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8.name());

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        String response = reader.readLine();

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        return json.get("access_token").getAsString();
    }

    private JsonObject getUserInfo(String accessToken) throws IOException {
        String urlStr = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        String response = reader.readLine();

        return JsonParser.parseString(response).getAsJsonObject();
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

        url.append(contextPath).append("/login-facebook");
        return url.toString();
    }
}