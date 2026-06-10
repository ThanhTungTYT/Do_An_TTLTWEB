package com.example.do_an_ttltweb.controller.adminPage1;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.services.AdminPage1Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "AdminPage1Servlet", urlPatterns = {"/admin/dashboard"})
public class AdminPage1Servlet extends HttpServlet {

    private AdminPage1Service service = new AdminPage1Service();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filter = request.getParameter("filter");

        if (filter == null || filter.isEmpty()) {
            filter = "today";
        }
        loadByFilter(request, filter);

        request.setAttribute("filter", filter);
        request.getRequestDispatcher("/adminPage1.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        if ((startDateStr != null && !startDateStr.isEmpty()) || (endDateStr != null && !endDateStr.isEmpty())) {

            Timestamp start;
            Timestamp end;

            if (startDateStr != null && !startDateStr.isEmpty()) {
                start = Timestamp.valueOf(startDateStr + " 00:00:00");
                request.setAttribute("startDate", startDateStr);
            } else {
                start = Timestamp.valueOf("1970-01-01 00:00:00");
                request.setAttribute("startDate", "");
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                end = Timestamp.valueOf(endDateStr + " 23:59:59");
                request.setAttribute("endDate", endDateStr);
            } else {
                end = new Timestamp(System.currentTimeMillis());

                String nowStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(end);
                request.setAttribute("endDate", nowStr);
            }

            loadByDate(request, start, end);
            request.setAttribute("filter", "custom");
        } else {
            loadByFilter(request, "today");
            request.setAttribute("filter", "today");
        }
        request.getRequestDispatcher("/adminPage1.jsp").forward(request, response);
    }

    private void loadByFilter(HttpServletRequest request, String filter) {

        request.setAttribute("totalRevenue", service.getTotalRevenue(filter));
        request.setAttribute("totalOrders", service.getTotalOrders(filter));
        request.setAttribute("pendingOrders", service.getPendingOrders(filter));
        request.setAttribute("newCustomers", service.getNewCustomers(filter));
        request.setAttribute("topProducts", service.getTopProducts(filter));
        request.setAttribute("worstProducts", service.getWorstProducts(filter));

        List<Order> orders = service.getOrders(filter);
        request.setAttribute("orders", orders);
    }

    private void loadByDate(HttpServletRequest request, Timestamp start, Timestamp end) {

        request.setAttribute("totalRevenue", service.getTotalRevenue(start, end));
        request.setAttribute("totalOrders", service.getTotalOrders(start, end));
        request.setAttribute("pendingOrders", service.getPendingOrders(start, end));
        request.setAttribute("newCustomers", service.getNewCustomers(start, end));
        request.setAttribute("topProducts", service.getTopProducts(start, end));
        request.setAttribute("worstProducts", service.getWorstProducts(start, end));

        List<Order> orders = service.getOrders(start, end);
        request.setAttribute("orders", orders);
    }
}
