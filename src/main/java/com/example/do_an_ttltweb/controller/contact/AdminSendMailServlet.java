package com.example.do_an_ttltweb.controller.contact;

import com.example.do_an_ttltweb.helper.email.EmailUtil;
import com.example.do_an_ttltweb.services.ContactService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AdminSendMailServlet", urlPatterns = {"/admin-send-mail"})
public class AdminSendMailServlet extends HttpServlet {

    private ContactService contactService;
    @Override
    public void init() {
        contactService = new ContactService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String toEmail = request.getParameter("toEmail");
        String toName = request.getParameter("toName");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        String contactId = request.getParameter("contactId");
        String page      = request.getParameter("page");
        String startDate = request.getParameter("startDate");
        String endDate   = request.getParameter("endDate");
        String state        = request.getParameter("state");


        String htmlContent = EmailUtil.buildReplyEmailTemplate(toName, content);

        HttpSession session = request.getSession();

        try {
            EmailUtil.sendEmailAsync(toEmail, subject, htmlContent);

            if (contactId != null && !contactId.isEmpty()) {
                contactService.updateState(Integer.parseInt(contactId), "DONE");
            }

            session.setAttribute("mailSuccess",
                    "Hệ thống đang tiến hành gửi phản hồi tới " + toEmail
                            + ". Trạng thái liên hệ đã chuyển sang <b>Đã xử lý</b>.");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("mailError",
                    "Lỗi hệ thống khi thiết lập luồng gửi mail: " + e.getMessage());
        }

        StringBuilder url = new StringBuilder(request.getContextPath())
                .append("/admin/contact?page=")
                .append(page != null && !page.isEmpty() ? page : "1");

        if (startDate != null && !startDate.isEmpty()) url.append("&startDate=").append(startDate);
        if (endDate   != null && !endDate.isEmpty())   url.append("&endDate=").append(endDate);
        if (state     != null && !state.isEmpty())     url.append("&state=").append(state);

        response.sendRedirect(url.toString());
    }
}