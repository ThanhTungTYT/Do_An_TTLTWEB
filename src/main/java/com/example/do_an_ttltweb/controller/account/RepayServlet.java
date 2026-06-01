package com.example.do_an_ttltweb.controller.account;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.OrderItem;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.model.cart.CartItem;
import com.example.do_an_ttltweb.services.OrderService;
import com.example.do_an_ttltweb.services.ProductService;
import com.example.do_an_ttltweb.services.PromotionService;
import com.example.do_an_ttltweb.services.AccountService;
import com.example.do_an_ttltweb.model.Address;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/repay")
public class RepayServlet extends HttpServlet {

    private final OrderService   orderService   = new OrderService();
    private final ProductService productService = new ProductService();
    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/account");
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        Order target = orders.stream()
                .filter(o -> o.getId() == orderId
                        && "Chờ thanh toán".equals(o.getStatus()))
                .findFirst()
                .orElse(null);

        if (target == null) {
            resp.sendRedirect(req.getContextPath() + "/account");
            return;
        }

        Cart checkoutCart = new Cart();
        List<OrderItem> items = target.getItems();

        if (items == null || items.isEmpty()) {
            items = orderService.getItemsByOrderId(orderId);
        }

        for (OrderItem item : items) {
            var product = productService.getProduct(item.getProductId());
            if (product == null) continue;

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(item.getQuantity());
            cartItem.setPrice(item.getPrice());
            checkoutCart.addItemDirectly(cartItem);
        }

        if (checkoutCart.getList().isEmpty()) {
            session.setAttribute("error", "Không thể tải lại sản phẩm của đơn hàng này.");
            resp.sendRedirect(req.getContextPath() + "/account");
            return;
        }

        session.setAttribute("checkoutCart",  checkoutCart);
        session.setAttribute("repayOrderId",  orderId);
        Address defaultAddress = accountService.getUserAddress(user.getId());

        req.setAttribute("userAddress",  defaultAddress);
        req.setAttribute("cart",         checkoutCart);
        req.setAttribute("promotions",   PromotionService.getInstance().getAvailablePromotions());
        req.setAttribute("repayOrderId", orderId);
        req.getRequestDispatcher("/payment.jsp").forward(req, resp);
    }
}