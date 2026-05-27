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

    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
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
            Pattern pattern = Pattern.compile("AROMACAFE\\s+(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                int orderId = Integer.parseInt(matcher.group(1));
                double transferAmount = amountStr != null ? Double.parseDouble(amountStr) : 0;

                Order order = orderService.getOrderById(orderId);

                if (order != null) {
                    if ("Chờ thanh toán".equals(order.getStatus())) {
                        if (Math.abs(order.getFinalAmount() - transferAmount) <= 100) {

                            String ghnOrderCode = null;
                            String nextStatus = "Đã thanh toán";
                            try {
                                OrderAddress address = orderService.getAddressByOrderId(orderId);
                                ghnOrderCode = orderService.createGhnOrderAfterPaid(order, address);
                                if (ghnOrderCode != null && !ghnOrderCode.isBlank()) {
                                    nextStatus = "Đang xử lý";
                                }
                            } catch (Exception e) {
                                System.out.println("Lỗi khi tự động đẩy đơn sang GHN: " + e.getMessage());
                            }

                            boolean updated = orderService.updateOrderStatusAndGhn(orderId, nextStatus, ghnOrderCode);

                            if (updated) {
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("{\"success\":true,\"message\":\"Order paid and sent to GHN\"}");
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"success\":false,\"message\":\"Process failed\"}");
    }

    private String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^,\"}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String val = matcher.group(1).trim();
            if(val.endsWith("\"")) {
                val = val.substring(0, val.length() - 1);
            }
            return val;
        }
        return null;
    }
}