package com.example.do_an_ttltweb.controller.order;

import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.model.cart.CartItem;
import com.example.do_an_ttltweb.model.*;
import com.example.do_an_ttltweb.services.AccountService;
import com.example.do_an_ttltweb.services.OrderService;
import com.example.do_an_ttltweb.services.PaymentMethodService;
import com.example.do_an_ttltweb.services.PromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/payment")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("OrderServlet doGet called. URI: " + req.getRequestURI());

        HttpSession s = req.getSession(false);

        if (s == null || s.getAttribute("checkoutCart") == null) {
            System.out.println("DEBUG: Missing checkoutCart, redirecting to cart.jsp");
            resp.sendRedirect("cart.jsp");
            return;
        }
        User user = (User) s.getAttribute("user");
        Address defaultAddress = accountService.getUserAddress(user.getId());

        req.setAttribute("userAddress", defaultAddress);
        req.setAttribute("cart", s.getAttribute("checkoutCart"));
        req.setAttribute("promotions", PromotionService.getInstance().getAvailablePromotions());
        req.getRequestDispatcher("payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("prepare".equals(action)) {
            handlePrepareCheckout(req, resp);
            return;
        }
        handleProcessPayment(req, resp);
    }

    private void handlePrepareCheckout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Cart mainCart = (Cart) session.getAttribute("cart");

        if (mainCart == null || mainCart.getTotalQuantity() == 0) {
            resp.sendRedirect("cart.jsp");
            return;
        }

        String[] selectedIds = req.getParameterValues("selectedIds");

        if (selectedIds == null || selectedIds.length == 0) {
            session.setAttribute("error", "Vui lòng chọn sản phẩm để thanh toán!");
            resp.sendRedirect("cart.jsp");
            return;
        }

        Cart checkoutCart = new Cart();
        for (String idStr : selectedIds) {
            try {
                int pid = Integer.parseInt(idStr);
                CartItem item = mainCart.getItem(pid);
                if (item != null) {
                    checkoutCart.addItemDirectly(item);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        session.setAttribute("checkoutCart", checkoutCart);

        resp.sendRedirect("payment");
    }

    private void handleProcessPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("user") == null || s.getAttribute("checkoutCart") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) s.getAttribute("user");
        Cart checkoutCart = (Cart) s.getAttribute("checkoutCart");
        Cart mainCart = (Cart) s.getAttribute("cart");

        double total = checkoutCart.getTotal();
        double shippingFee = 30000;
        double discountPercent = 0;

        Promotion promotion = null;
        String pid = req.getParameter("promotionId");
        if (pid != null && !pid.isBlank()) {
            try {
                promotion = PromotionService.getInstance().getPromotionById(Integer.parseInt(pid));
                if (promotion != null && "active".equals(promotion.getState()) && promotion.getQuantity() > 0 && total >= promotion.getMinOrderValue()) {
                    discountPercent = promotion.getDiscountPercent();
                    if (discountPercent > 100) discountPercent = 100;
                } else {
                    promotion = null;
                }
            } catch (Exception e) { promotion = null; }
        }
        if (promotion == null) {
            promotion = PromotionService.getInstance().getNoPromo();
        }

        double discountAmount = total * discountPercent / 100;
        double finalAmount = total - discountAmount + shippingFee;

        Order order = new Order();
        order.setUserId(user.getId());
        order.setReceiverName(req.getParameter("fullname"));
        order.setReceiverPhone(req.getParameter("phone"));
        order.setNote(req.getParameter("note") == null ? "" : req.getParameter("note"));

        String paymentName = req.getParameter("paymentMethod");
        int paymentMethodId = PaymentMethodService.getInstance().getPaymentMethodId(paymentName);
        order.setPaymentMethodId(paymentMethodId);

        order.setPromoId(promotion.getId());
        order.setTotalAmount(total);
        order.setShippingFee(shippingFee);
        order.setDiscountPercent(discountPercent);
        order.setFinalAmount(finalAmount);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Đang xử lý");

        OrderAddress address = new OrderAddress();
        address.setCountry(req.getParameter("country"));
        address.setProvince(req.getParameter("province"));
        address.setWard(req.getParameter("ward"));
        address.setAddress(req.getParameter("address"));

        try {
            boolean success;
            if (orderService.create(order, address, checkoutCart)) success = true;
            else success = false;
            if (success) {
                for (CartItem boughtItem : checkoutCart.getList()) {
                    mainCart.remove(boughtItem.getProduct().getId());
                }

                s.setAttribute("cart", mainCart);

                s.removeAttribute("checkoutCart");

                resp.sendRedirect(req.getContextPath() + "/account?success=1");
            } else {
                req.setAttribute("error", "Đặt hàng thất bại. Vui lòng thử lại.");
                req.getRequestDispatcher("payment.jsp").forward(req, resp);
            }
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("payment.jsp").forward(req, resp);
        }
    }
}