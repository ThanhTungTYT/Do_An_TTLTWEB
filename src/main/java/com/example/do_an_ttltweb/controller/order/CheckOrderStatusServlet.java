package com.example.do_an_ttltweb.controller.order;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.services.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/check-order-status")
public class CheckOrderStatusServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isBlank()) {
            resp.getWriter().write("{\"status\":\"Không tìm thấy ID\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderService.getOrderById(orderId);

            if (order != null) {
                resp.getWriter().write("{\"status\":\"" + order.getStatus() + "\"}");
            } else {
                resp.getWriter().write("{\"status\":\"Đơn hàng không tồn tại\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"status\":\"Lỗi hệ thống\"}");
        }
    }
}
