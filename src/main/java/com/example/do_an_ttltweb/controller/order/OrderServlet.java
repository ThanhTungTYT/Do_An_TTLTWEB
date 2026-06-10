package com.example.do_an_ttltweb.controller.order;

import com.example.do_an_ttltweb.dao.CartDao;
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
import java.util.List;

@WebServlet("/payment")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final AccountService accountService = new AccountService();
    private final CartDao cartDao = new CartDao();

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

        switch (action == null ? "" : action) {
            case "prepare":
                handlePrepareCheckout(req, resp);
                break;
            default:
                handleProcessPayment(req, resp);
        }
    }

    private void handlePrepareCheckout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
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

        List<String> stockErrors = cartDao.validateStock(checkoutCart);
        if (!stockErrors.isEmpty()) {
            session.setAttribute("error", stockErrors.get(0));
            resp.sendRedirect("cart.jsp");
            return;
        }

        session.setAttribute("checkoutCart", checkoutCart);
        resp.sendRedirect("payment");
    }

    private void handleProcessPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession s = req.getSession(false);
        String paymentMethodName = req.getParameter("paymentMethod");
        boolean isBankTransfer = "bank".equals(paymentMethodName);

        if (s == null || s.getAttribute("user") == null) {
            if (isBankTransfer) sendJsonError(resp, "Phiên làm việc đã hết hạn. Vui lòng thử lại.");
            else resp.sendRedirect("login.jsp");
            return;
        }

        if (s.getAttribute("checkoutCart") == null) {
            if (isBankTransfer) {
                sendJsonError(resp, "Giỏ hàng trống hoặc đã được xử lý. Vui lòng quay lại giỏ hàng.");
            } else {
                resp.sendRedirect("cart.jsp");
            }
            return;
        }

        Integer repayOrderId = (Integer) s.getAttribute("repayOrderId");
        if (repayOrderId != null) {
            s.removeAttribute("repayOrderId");
            s.removeAttribute("checkoutCart");
            if (isBankTransfer) {
                Order existingOrder = orderService.getOrderById(repayOrderId);
                long finalAmount = existingOrder != null ? Math.round(existingOrder.getFinalAmount()) : 0;
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("{\"success\":true,\"orderId\":" + repayOrderId + ",\"finalAmount\":" + finalAmount + "}");
            } else {
                orderService.updateOrderStatusById(repayOrderId, "Đang xử lý");
                s.setAttribute("success", "Đặt hàng thành công!");
                resp.sendRedirect(req.getContextPath() + "/account");
            }
            return;
        }

        User user = (User) s.getAttribute("user");
        Cart checkoutCart = (Cart) s.getAttribute("checkoutCart");
        Cart mainCart = (Cart) s.getAttribute("cart");

        Order       order   = buildOrder(req, user, checkoutCart);
        OrderAddress address = buildAddress(req);

        if (isBankTransfer) {
            order.setStatus("Chờ thanh toán");
        }

        try {
            if (orderService.create(order, address, checkoutCart)) {

                if (isBankTransfer) {

                    s.setAttribute("pendingBankOrderId", order.getId());

                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");

                    long finalAmountLong = Math.round(order.getFinalAmount());
                    String jsonResponse = "{\"success\":true,\"orderId\":" + order.getId() + ",\"finalAmount\":" + finalAmountLong + "}";
                    resp.getWriter().write(jsonResponse);
                    cleanupAfterOrder(s, mainCart, checkoutCart, user);
                } else {
                    cleanupAfterOrder(s, mainCart, checkoutCart, user);
                    s.setAttribute("success", "Đặt hàng thành công!");
                    resp.sendRedirect(req.getContextPath() + "/account");
                }
            } else {
                if (isBankTransfer) {
                    sendJsonError(resp, "Đặt hàng thất bại từ hệ thống. Vui lòng thử lại.");
                } else {
                    forwardWithError(req, resp, s, "Đặt hàng thất bại. Vui lòng thử lại.");
                }
            }
        } catch (RuntimeException e) {
            if (isBankTransfer) {
                sendJsonError(resp, e.getMessage());
            } else {
                forwardWithError(req, resp, s, e.getMessage());
            }
        }
    }

    private void sendJsonError(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String cleanMessage = message.replace("\"", "\\\"");
        resp.getWriter().write("{\"success\":false,\"message\":\"" + cleanMessage + "\"}");
    }

    private Order buildOrder(HttpServletRequest req, User user, Cart checkoutCart) {
        double total = checkoutCart.getTotal();
        double shippingFee = 0;
        String shippingFeeParam = req.getParameter("shippingFee");
        if (shippingFeeParam != null && !shippingFeeParam.isBlank()) {
            try {
                shippingFee = Double.parseDouble(shippingFeeParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Promotion promotion = null;
        double discountPercent = 0;
        String pid = req.getParameter("promotionId");

        if (pid != null && !pid.isBlank()) {
            try {
                promotion = PromotionService.getInstance().getPromotionById(Integer.parseInt(pid));
                if (promotion != null
                        && "active".equals(promotion.getState())
                        && promotion.getQuantity() > 0
                        && total >= promotion.getMinOrderValue()) {
                    discountPercent = Math.min(promotion.getDiscountPercent(), 100);
                } else {
                    promotion = null;
                }
            } catch (Exception e) {
                promotion = null;
            }
        }
        if (promotion == null) {
            promotion = PromotionService.getInstance().getNoPromo();
        }

        double discountAmount = total * discountPercent / 100;
        double finalAmount    = total - discountAmount + shippingFee;

        String paymentName    = req.getParameter("paymentMethod");
        int    paymentMethodId = PaymentMethodService.getInstance().getPaymentMethodId(paymentName);

        Order order = new Order();
        order.setUserId(user.getId());
        order.setReceiverName(req.getParameter("fullname"));
        order.setReceiverPhone(req.getParameter("phone"));
        order.setNote(req.getParameter("note") == null ? "" : req.getParameter("note"));
        order.setPaymentMethodId(paymentMethodId);
        order.setPromoId(promotion.getId());
        order.setTotalAmount(total);
        order.setShippingFee(shippingFee);
        order.setDiscountPercent(discountPercent);
        order.setFinalAmount(finalAmount);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Đang xử lý");
        return order;
    }

    private OrderAddress buildAddress(HttpServletRequest req) {
        OrderAddress address = new OrderAddress();
        address.setCountry(req.getParameter("country"));
        address.setProvince(req.getParameter("province"));
        address.setDistrict(req.getParameter("district"));
        address.setWard(req.getParameter("ward"));
        address.setAddress(req.getParameter("address"));

        String districtIdStr = req.getParameter("district_id");
        if (districtIdStr != null && !districtIdStr.isEmpty()) {
            try { address.setDistrictId(Integer.parseInt(districtIdStr)); }
            catch (Exception e) { address.setDistrictId(0); }
        }
        address.setWardCode(req.getParameter("ward_code"));
        return address;
    }

    private void cleanupAfterOrder(HttpSession s, Cart mainCart, Cart checkoutCart, User user) {
        for (CartItem boughtItem : checkoutCart.getList()) {
            mainCart.remove(boughtItem.getProduct().getId());
            cartDao.removeItem(user.getId(), boughtItem.getProduct().getId());
        }
        s.setAttribute("cart", mainCart);
        s.removeAttribute("checkoutCart");
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp,
                                  HttpSession s, String errorMsg)
            throws ServletException, IOException {
        req.setAttribute("error", errorMsg);
        req.setAttribute("cart",       s.getAttribute("checkoutCart"));
        req.setAttribute("promotions", PromotionService.getInstance().getAvailablePromotions());
        req.getRequestDispatcher("payment.jsp").forward(req, resp);
    }
}