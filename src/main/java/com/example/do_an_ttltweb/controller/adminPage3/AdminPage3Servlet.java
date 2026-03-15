package com.example.do_an_ttltweb.controller.adminPage3;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.OrderItem;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AccountService;
import com.example.do_an_ttltweb.services.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "AdminPage3Servlet", value = "/admin/orders")


public class AdminPage3Servlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        int page = 1;
        int pageSize = 25; // 25 đơn hàng mỗi trang

        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalOrders = orderService.countOrders(startDate, endDate);
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
        if (totalPages < 1) totalPages = 1;

        List<Order> orders = orderService.getOrdersPagination(startDate, endDate, page, pageSize);

        List<User> users = accountService.getAllUser();
        Map<Integer, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u, (existing, replacement) -> existing));

        Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();
        for (Order o : orders) {
            orderItemsMap.put(
                    o.getId(),
                    orderService.getItemsByOrderId(o.getId())
            );
        }

        req.setAttribute("orders", orders);
        req.setAttribute("userMap", userMap);
        req.setAttribute("orderItemsMap", orderItemsMap);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);

        req.getRequestDispatcher("/adminPage3.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String status = "Đã giao";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(status);

        boolean updated = orderService.updateOrder(order);
        System.out.println("Update order " + orderId + ": " + updated);

        orderService.updateOrder(order);
        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}