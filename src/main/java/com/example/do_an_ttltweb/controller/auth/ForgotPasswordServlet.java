package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.services.AuthService;
import com.example.do_an_ttltweb.services.ContactService;
import com.example.do_an_ttltweb.helper.email.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "ForgotPasswordServlet", value = "/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private static final Map<String, Long> requestCache = new ConcurrentHashMap<>();
    private static final long COOLDOWN_TIME = 3600 * 1000; // Thời gian chờ reset mỗi 1 tiếng

    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null) email = "";
        email = email.trim();

        if (requestCache.containsKey(email)) {
            long lastRequestTime = requestCache.get(email);
            if (System.currentTimeMillis() - lastRequestTime < COOLDOWN_TIME) {
                request.setAttribute("error", "Bạn vừa yêu cầu đổi mật khẩu. Vui lòng thử lại sau 1 tiếng.");
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                return;
            }
        }

        if (!authService.existsByEmail(email)) {
            request.setAttribute("error", "Email này chưa được đăng ký trong hệ thống!");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
            return;
        }

        try {
            String newPasswordRaw = generateStrongPassword();

            boolean isUpdated = authService.resetPassword(email, newPasswordRaw);

            if (isUpdated) {
                String subject = "Cấp lại mật khẩu mới - Aroma Café";
                String body = "<h3>Xin chào,</h3>" +
                        "<p>Mật khẩu mới của bạn là: <strong style='font-size: 18px;'>" + newPasswordRaw + "</strong></p>" +
                        "<p>Vui lòng đăng nhập và đổi lại mật khẩu ngay để bảo mật thông tin.</p>";

                EmailUtil.sendEmail(email, subject, body);

                requestCache.put(email, System.currentTimeMillis());

                HttpSession session = request.getSession();
                session.setAttribute("successMsg", "Mật khẩu mới đã được gửi vào email. Vui lòng kiểm tra hộp thư.");

                response.sendRedirect(request.getContextPath() + "/login.jsp");
            } else {
                request.setAttribute("error", "Lỗi hệ thống: Không thể cập nhật mật khẩu.");
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
        }
    }

    private String generateStrongPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}