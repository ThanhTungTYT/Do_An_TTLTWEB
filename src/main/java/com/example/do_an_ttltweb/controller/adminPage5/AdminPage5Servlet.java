package com.example.do_an_ttltweb.controller.adminPage5;

import com.example.do_an_ttltweb.services.ContactService;
import com.example.do_an_ttltweb.model.Contact;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminContactServlet", urlPatterns = {"/admin/contact"})
public class AdminPage5Servlet extends HttpServlet {

    private ContactService contactService;

    @Override
    public void init() {
        contactService = new ContactService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        int page = 1;
        int pageSize = 25;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        int totalContacts = contactService.getTotalContacts(startDate, endDate);
        List<Contact> contactList = contactService.getContactList(startDate, endDate, pageSize, offset);

        int totalPages = (int) Math.ceil((double) totalContacts / pageSize);
        if (totalPages < 1) {
            totalPages = 1;
        }

        request.setAttribute("contactList", contactList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        request.getRequestDispatcher("/adminPage5.jsp").forward(request, response);
    }
}