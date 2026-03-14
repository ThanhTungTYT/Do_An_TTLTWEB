package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.helper.email.EmailUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "ResendOtpServlet", value = "/resend-otp")
public class ResendOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        @SuppressWarnings("unchecked")
        Map<String, Object> regData = (Map<String, Object>) session.getAttribute("reg_temp");

        if (regData == null) {
            response.sendRedirect(request.getContextPath() + "/register.jsp");
            return;
        }

        long currentTime = System.currentTimeMillis();
        long nextResend = (long) regData.get("nextResend");

        if (currentTime < nextResend) {
            long waitSeconds = (nextResend - currentTime) / 1000;
            request.setAttribute("error", "Vui lòng chờ " + waitSeconds + " giây nữa để gửi lại mã.");
            request.getRequestDispatcher("verify_otp.jsp").forward(request, response);
            return;
        }

        try {
            Random rnd = new Random();
            int number = rnd.nextInt(1000000);
            String newOtp = String.format("%06d", number);

            String email = (String) regData.get("email");
            String fullname = (String) regData.get("fullname");

            String subject = "Gửi lại mã xác thực - Aroma Café";
            String body = "<h3>Xin chào " + fullname + ",</h3>" +
                    "<p>Bạn vừa yêu cầu gửi lại mã xác thực.</p>" +
                    "<p>Mã OTP mới của bạn là: <strong style='color: #c76739; font-size: 18px;'>" + newOtp + "</strong></p>" +
                    "<p>Mã có hiệu lực trong 10 phút tới.</p>";

            EmailUtil.sendEmail(email, subject, body);

            regData.put("otp", newOtp);
            regData.put("expireTime", currentTime + (10 * 60 * 1000));
            regData.put("nextResend", currentTime + (2 * 60 * 1000));

            session.setAttribute("reg_temp", regData);
            request.setAttribute("message", "Đã gửi mã OTP mới vào email của bạn.");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi gửi email! Vui lòng thử lại sau.");
        }

        request.getRequestDispatcher("verify_otp.jsp").forward(request, response);
    }
}