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

        String contactId = request.getParameter("contactId");
        String page      = request.getParameter("page");
        String startDate = request.getParameter("startDate");
        String endDate   = request.getParameter("endDate");

        String htmlContent = EmailUtil.buildReplyEmailTemplate(toName, content);

        HttpSession session = request.getSession();

        try {
            EmailUtil.sendEmailAsync(toEmail, subject, htmlContent);

            session.setAttribute("success", "Hệ thống đang tiến hành gửi phản hồi tới " + toEmail + ". Quá trình này sẽ hoàn tất trong giây lát.");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi hệ thống khi thiết lập luồng gửi mail: " + e.getMessage());
        }

        StringBuilder url = new StringBuilder(request.getContextPath())
                .append("/admin/contact/delete?id=")
                .append(contactId != null ? contactId : "");

        if (page != null && !page.isEmpty())
            url.append("&page=").append(page);
        if (startDate != null && !startDate.isEmpty())
            url.append("&startDate=").append(startDate);
        if (endDate != null && !endDate.isEmpty())
            url.append("&endDate=").append(endDate);

        response.sendRedirect(url.toString());
    }
}