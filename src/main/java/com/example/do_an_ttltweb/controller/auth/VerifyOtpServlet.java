package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "VerifyOtpServlet", value = "/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("reg_temp") == null) {
            response.sendRedirect(request.getContextPath() + "/register.jsp");
            return;
        }
        request.getRequestDispatcher("verify_otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        @SuppressWarnings("unchecked")
        Map<String, Object> regData = (Map<String, Object>) session.getAttribute("reg_temp");

        if (regData == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        long expireTime = (long) regData.get("expireTime");
        if (System.currentTimeMillis() > expireTime) {
            session.removeAttribute("reg_temp");
            request.setAttribute("status", "Mã xác thực đã hết hạn. Vui lòng đăng ký lại.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String inputOtp = request.getParameter("otp");
        String sessionOtp = (String) regData.get("otp");

        if (inputOtp != null && inputOtp.equals(sessionOtp)) {
            User user = new User();
            user.setFull_name((String) regData.get("fullname"));
            user.setEmail((String) regData.get("email"));
            user.setPhone((String) regData.get("phone"));
            user.setPassword_hash((String) regData.get("password"));

            if (authService.register(user)) {
                session.removeAttribute("reg_temp");
                session.setAttribute("successMsg", "Đăng ký thành công! Vui lòng đăng nhập.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.setAttribute("error", "Lỗi hệ thống khi tạo tài khoản.");
                request.getRequestDispatcher("verify_otp.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Mã xác thực không chính xác!");
            request.getRequestDispatcher("verify_otp.jsp").forward(request, response);
        }
    }
}