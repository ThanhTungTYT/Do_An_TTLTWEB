package com.example.do_an_ttltweb.controller.adminPage5;

import com.example.do_an_ttltweb.services.ContactService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteContactServlet", urlPatterns = {"/admin/contact/delete", "/admin/contact/delete-bulk"})
public class DeleteContactServlet extends HttpServlet {

    private ContactService contactService;

    @Override
    public void init() {
        contactService = new ContactService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        handleDeleteSingle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();

        if (uri.endsWith("/admin/contact/delete-bulk")) {
            handleDeleteBulk(request, response);
        } else {
            handleDeleteSingle(request, response);
        }
    }

    private void handleDeleteSingle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                contactService.deleteContact(Integer.parseInt(idStr));
            } catch (NumberFormatException ignored) { }
        }
        redirectToList(request, response);
    }


    private void handleDeleteBulk(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        contactService.deleteContacts(request.getParameterValues("ids"));
        redirectToList(request, response);
    }


    private void redirectToList(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String page      = request.getParameter("page");
        String startDate = request.getParameter("startDate");
        String endDate   = request.getParameter("endDate");

        StringBuilder url = new StringBuilder(request.getContextPath())
                .append("/admin/contact?page=")
                .append(page != null && !page.isEmpty() ? page : "1");

        if (startDate != null && !startDate.isEmpty())
            url.append("&startDate=").append(startDate);
        if (endDate != null && !endDate.isEmpty())
            url.append("&endDate=").append(endDate);

        response.sendRedirect(url.toString());
    }
}