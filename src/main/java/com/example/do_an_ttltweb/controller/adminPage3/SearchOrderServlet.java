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

@WebServlet(name = "SearchOrderServlet", value = "/admin/orders/search")
public class SearchOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("search");
        List<Order> orders;

        if (keyword != null && !keyword.trim().isEmpty()) {
            orders = orderService.searchOrders(keyword.trim());
        } else {
            orders = orderService.getOrdersPagination(null, null, 1, 25);
        }

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
        req.setAttribute("searchKeyword", keyword);
        req.setAttribute("currentPage", 1);
        req.setAttribute("totalPages", 1);

        req.getRequestDispatcher("/adminPage3.jsp").forward(req, resp);
    }
}