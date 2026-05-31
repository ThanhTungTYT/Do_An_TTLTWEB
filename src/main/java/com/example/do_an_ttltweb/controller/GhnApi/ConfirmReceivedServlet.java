package com.example.do_an_ttltweb.controller.GhnApi;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ConfirmReceivedServlet", value = "/confirm-received")
public class ConfirmReceivedServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.getOrderById(orderId);

        if (order != null
                && order.getUserId() == user.getId()
                && "Đang giao".equals(order.getStatus())) {
            orderService.updateOrderStatusAndGhn(orderId, "Đã giao", order.getGhnOrderCode());
            request.getSession().setAttribute("success", "Đã xác nhận nhận hàng!");
        } else {
            request.getSession().setAttribute("error", "Xác nhận nhận hàng thất bại!");
        }

        response.sendRedirect(request.getContextPath() + "/account");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}