package com.example.do_an_ttltweb.servlet;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/login-facebook")
public class FacebookLoginServlet extends HttpServlet {

    private static final String APP_ID ="1254182139570503";
    private static final String APP_SECRET ="66ef26108918fa42d8f8827c8c4fdc6c";
    private static final String REDIRECT_URI ="http://localhost:8080/Do_An_TTLTWEB/login-facebook";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String code = request.getParameter("code");

        if (code == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String accessToken = getAccessToken(code);
            JsonObject fbUser = getUserInfo(accessToken);

            String email = fbUser.has("email") ? fbUser.get("email").getAsString() : null;
            String name = fbUser.get("name").getAsString();

            AuthDao authDao = new AuthDao();
            User user = null;

            if (email != null) {
                user = authDao.findByEmail(email);
            }

            // 🆕 nếu chưa có → tạo
            if (user == null) {
                user = new User();
                user.setFull_name(name);
                user.setEmail(email);
                user.setPhone("");
                user.setPassword_hash(null);

                authDao.register(user);

                if (email != null) {
                    user = authDao.findByEmail(email);
                }
            }

            request.getSession().setAttribute("user", user);
            response.sendRedirect("http://localhost:8080/Do_An_TTLTWEB/");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login Facebook thất bại: " + e.getMessage());
        }
    }

    // ================= TOKEN =================
    private String getAccessToken(String code) throws IOException {
        String urlStr = "https://graph.facebook.com/v18.0/oauth/access_token"
                + "?client_id=" + APP_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&client_secret=" + APP_SECRET
                + "&code=" + code;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        String response = reader.readLine();

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        return json.get("access_token").getAsString();
    }

    // ================= USER INFO =================
    private JsonObject getUserInfo(String accessToken) throws IOException {
        String urlStr = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        String response = reader.readLine();

        return JsonParser.parseString(response).getAsJsonObject();
    }
}