package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.services.AuthService;
import com.example.do_an_ttltweb.helper.email.EmailUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "register", value = "/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private static final int MIN_PASS_LENGTH = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");

        if (authService.existsByEmail(email)) {
            request.setAttribute("error", "Email đã tồn tại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!Validation.isEmail(email)) {
            request.setAttribute("error", "Email không hợp lệ!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!Validation.isPhone(phone)) {
            request.setAttribute("error", "Số điện thoại không hợp lệ (Phải là 10 chữ số)!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!Validation.passLength(password, MIN_PASS_LENGTH)) {
            request.setAttribute("error", "Mật khẩu phải từ 8 ký tự trở lên!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!Validation.containChar(password)) {
            request.setAttribute("error", "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!Validation.rePass(password, confirmPassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            Random rnd = new Random();
            int number = rnd.nextInt(1000000);
            String otp = String.format("%06d", number);

            String subject = "Mã xác thực đăng ký tài khoản";
            String body = "<h3>Xin chào " + fullname + ",</h3>" +
                    "<p>Mã xác thực (OTP) của bạn là: <strong style='color: #c76739; font-size: 18px;'>" + otp + "</strong></p>" +
                    "<p>Mã có hiệu lực trong 10 phút.</p>";

            EmailUtil.sendEmail(email, subject, body);

            Map<String, Object> regData = new HashMap<>();
            regData.put("fullname", fullname);
            regData.put("email", email);
            regData.put("phone", phone);
            regData.put("password", password);
            regData.put("otp", otp);

            long currentTime = System.currentTimeMillis();
            regData.put("expireTime", currentTime + (10 * 60 * 1000));
            regData.put("nextResend", currentTime + (2 * 60 * 1000));

            request.getSession().setAttribute("reg_temp", regData);
            response.sendRedirect(request.getContextPath() + "/verify-otp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi gửi email! Kiểm tra lại địa chỉ email hoặc kết nối mạng.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}