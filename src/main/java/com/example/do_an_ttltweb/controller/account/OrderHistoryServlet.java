package com.example.do_an_ttltweb.controller.account;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.OrderService;
import com.example.do_an_ttltweb.model.OrderAddress;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderHistoryServlet", urlPatterns = {"/his-order", "/cancel-order"})
public class OrderHistoryServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;
    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User authUser = (User) session.getAttribute("user");

        if (authUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Phân trang
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (NumberFormatException ignored) {}

        List<Order> allOrders = orderService.getOrdersByUserId(authUser.getId());
        int totalOrders = allOrders.size();
        int totalPages  = (int) Math.ceil((double) totalOrders / PAGE_SIZE);
        if (page < 1) page = 1;
        if (totalPages > 0 && page > totalPages) page = totalPages;

        int from = (page - 1) * PAGE_SIZE;
        int to   = Math.min(from + PAGE_SIZE, totalOrders);
        List<Order> orders = allOrders.subList(from, to);

        Map<Integer, OrderAddress> orderAddressMap = new HashMap<>();
        for (Order o : orders) {
            OrderAddress addr = orderService.getAddressByOrderId(o.getId());
            if (addr != null) orderAddressMap.put(o.getId(), addr);
        }
        request.setAttribute("orderAddressMap", orderAddressMap);
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/historyOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User authUser = (User) session.getAttribute("user");

        if (authUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String orderIdStr = request.getParameter("orderId");
        boolean cancelled = false;
        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                orderService.cancelOrder(orderId, authUser.getId());
                cancelled = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (cancelled) {
            session.setAttribute("success", "Đã hủy đơn hàng thành công!");
        } else {
            session.setAttribute("error", "Hủy đơn hàng thất bại!");
        }
        response.sendRedirect(request.getContextPath() + "/account");
    }
}
