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

        List<Order> orders = orderService.getOrdersByUserId(authUser.getId());
        Map<Integer, OrderAddress> orderAddressMap = new HashMap<>();
        for (Order o : orders) {
            OrderAddress addr = orderService.getAddressByOrderId(o.getId());
            if (addr != null) orderAddressMap.put(o.getId(), addr);
        }
        request.setAttribute("orderAddressMap", orderAddressMap);
        request.setAttribute("orders", orders);

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
