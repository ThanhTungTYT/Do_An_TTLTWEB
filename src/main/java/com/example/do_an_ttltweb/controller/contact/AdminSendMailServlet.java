package com.example.do_an_ttltweb.controller.contact;

import com.example.do_an_ttltweb.helper.email.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AdminSendMailServlet", urlPatterns = {"/admin-send-mail"})
public class AdminSendMailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String toEmail = request.getParameter("toEmail");
        String toName = request.getParameter("toName");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        String htmlContent = EmailUtil.buildReplyEmailTemplate(toName, content);

        HttpSession session = request.getSession();

        try {
            EmailUtil.sendEmail(toEmail, subject, htmlContent);

            session.setAttribute("msgSuccess", "Đã gửi phản hồi thành công tới " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgError", "Gửi thất bại: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/contact");
    }
}