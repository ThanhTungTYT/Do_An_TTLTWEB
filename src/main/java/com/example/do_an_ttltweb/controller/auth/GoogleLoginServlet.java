package com.example.do_an_ttltweb.servlet;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.GoogleUser;
import com.example.do_an_ttltweb.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@WebServlet("/login-google")
public class GoogleLoginServlet extends HttpServlet {

    private static final String CLIENT_ID = "682060554420-qs22m1250tphcaablu0m653jo1s59n7j.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/Do_An_TTLTWEB/login-google";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String code = request.getParameter("code");

        if (code == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 🔥 Lấy JSON token
            JsonObject tokenJson = getTokenJson(code);

            // 🔥 Lấy user từ id_token
            GoogleUser ggUser = parseUserFromIdToken(tokenJson.get("id_token").getAsString());

            AuthDao authDao = new AuthDao();

            // 🔍 tìm user trong DB
            User user = authDao.findByEmail(ggUser.getEmail());

            // 🆕 nếu chưa có → tạo mới
            if (user == null) {
                user = new User();
                user.setFull_name(ggUser.getName());
                user.setEmail(ggUser.getEmail());
                user.setPhone(null);
                user.setPassword_hash(null);

                authDao.register(user);

                // lấy lại user
                user = authDao.findByEmail(ggUser.getEmail());
            }

            // ✅ lưu session
            request.getSession().setAttribute("user", user);

            response.sendRedirect("http://localhost:8080/Do_An_TTLTWEB/");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login Google thất bại: " + e.getMessage());
        }
    }

    // ================== LẤY TOKEN ==================
    private JsonObject getTokenJson(String code) throws IOException {

        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String params = "code=" + code +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + REDIRECT_URI +
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

    // ================== PARSE USER ==================
    private GoogleUser parseUserFromIdToken(String idToken) {

        String[] parts = idToken.split("\\.");

        if (parts.length < 2) {
            throw new RuntimeException("id_token không hợp lệ");
        }

        // decode payload
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

        JsonObject json = JsonParser.parseString(payload).getAsJsonObject();

        GoogleUser user = new GoogleUser();
        user.setId(json.get("sub").getAsString());
        user.setEmail(json.get("email").getAsString());
        user.setName(json.get("name").getAsString());

        return user;
    }
}