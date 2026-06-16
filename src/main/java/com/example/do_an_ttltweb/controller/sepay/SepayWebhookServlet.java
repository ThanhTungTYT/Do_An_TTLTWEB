package com.example.do_an_ttltweb.controller.sepay;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.OrderAddress;
import com.example.do_an_ttltweb.services.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/api/sepay-webhook")
public class SepayWebhookServlet extends HttpServlet {

    private String SEPAY_API_KEY;

    @Override
    public void init() {
        SEPAY_API_KEY = getServletContext().getInitParameter("SEPAY_API_KEY");
    }
    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String authHeader = req.getHeader("Authorization");
        if (SEPAY_API_KEY != null && !("Apikey " + SEPAY_API_KEY).equals(authHeader)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"success\":false,\"message\":\"Unauthorized\"}");
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        String jsonPayload = sb.toString();
        System.out.println("LOG SEPAY WEBHOOK RECEIVED: " + jsonPayload);

        String content = extractJsonValue(jsonPayload, "content");
        String amountStr = extractJsonValue(jsonPayload, "transferAmount");

        if (content == null || content.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":false,\"message\":\"Missing transaction content\"}");
            return;
        }

        try {
            Pattern pattern = Pattern.compile("AROMACAFE\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);

            if (!matcher.find()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"success\":true,\"message\":\"Not our transaction\"}");
                return;
            }

            int orderId = Integer.parseInt(matcher.group(1));
            double transferAmount = amountStr != null ? Double.parseDouble(amountStr.trim()) : 0;

            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                resp.setStatus(HttpServletResponse.SC_OK); // trả 200 tránh retry
                resp.getWriter().write("{\"success\":false,\"message\":\"Order not found\"}");
                return;
            }

            if (!"Chờ thanh toán".equals(order.getStatus())) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"success\":true,\"message\":\"Already processed\"}");
                return;
            }

            if (Math.abs(order.getFinalAmount() - transferAmount) > 100) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"success\":false,\"message\":\"Amount mismatch\"}");
                return;
            }

            String ghnOrderCode = null;
            String nextStatus = "Đang xử lý";
            try {
                OrderAddress address = orderService.getAddressByOrderId(orderId);
                ghnOrderCode = orderService.createGhnOrderAfterPaid(order, address);
                if (ghnOrderCode != null && !ghnOrderCode.isBlank()) {
                    nextStatus = "Đang giao";
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi tự động đẩy đơn sang GHN: " + e.getMessage());
            }

            boolean updated = orderService.confirmPaymentAndDeductStock(orderId, nextStatus, ghnOrderCode);

            if (updated) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"success\":true,\"message\":\"Order paid and sent to GHN\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\":false,\"message\":\"DB update failed\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\":false,\"message\":\"Internal error\"}");
        }
    }

    private String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^,}\"]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}