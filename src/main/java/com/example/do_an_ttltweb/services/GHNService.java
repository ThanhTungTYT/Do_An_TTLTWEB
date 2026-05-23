package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.OrderAddress;
import com.example.do_an_ttltweb.model.OrderItem;
import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GHNService {

    private static final String TOKEN = "6afa2c0e-5455-11f1-8e41-02a99c52882d";
    private static final String SHOP_ID = "6447396";
    private static final String BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api";

    private JsonObject callGet(String endpoint) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Token", TOKEN);
        conn.setRequestProperty("Content-Type", "application/json");

        return handleResponse(conn);
    }

    private JsonObject callPost(String endpoint, String body) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", TOKEN);
        conn.setRequestProperty("ShopId", SHOP_ID);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        return handleResponse(conn);
    }

    private JsonObject handleResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();

        InputStream is = (responseCode >= 400) ? conn.getErrorStream() : conn.getInputStream();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        String responseBody = sb.toString().trim();

        if (responseCode >= 400) {
            System.err.println("GHN API Error - Code: " + responseCode);
            System.err.println("Endpoint: " + conn.getURL());
            System.err.println("Response: " + responseBody);
            throw new IOException("GHN API Error: " + responseCode + " - " + responseBody);
        }

        if (responseBody.isEmpty()) {
            throw new IOException("Empty response from GHN API");
        }

        return JsonParser.parseString(responseBody).getAsJsonObject();
    }

    public JsonArray getProvinces() throws IOException {
        return callGet("/master-data/province").getAsJsonArray("data");
    }

    public JsonArray getDistricts(int provinceId) throws IOException {
        return callPost("/master-data/district",
                "{\"province_id\":" + provinceId + "}").getAsJsonArray("data");
    }

    public JsonArray getWards(int districtId) throws IOException {
        return callPost("/master-data/ward",
                "{\"district_id\":" + districtId + "}").getAsJsonArray("data");
    }

    public int calculateFee(int toDistrictId, String toWardCode, int weightGram)
            throws IOException {
        String body = "{"
                + "\"service_type_id\":2,"
                + "\"to_district_id\":" + toDistrictId + ","
                + "\"to_ward_code\":\"" + toWardCode + "\","
                + "\"weight\":" + weightGram
                + "}";
        JsonObject res = callPost("/v2/shipping-order/fee", body);
        return res.getAsJsonObject("data").get("total").getAsInt();
    }

    public int getServiceId(int fromDistrictId, int toDistrictId) throws IOException {
        String body = "{"
                + "\"shop_id\":" + SHOP_ID + ","
                + "\"from_district\":" + fromDistrictId + ","
                + "\"to_district\":" + toDistrictId
                + "}";

        System.out.println("Get services body: " + body);
        JsonObject res = callPost("/v2/shipping-order/available-services", body);
        System.out.println("Available services: " + res.toString());

        JsonArray services = res.getAsJsonArray("data");
        if (services != null && services.size() > 0) {
            return services.get(0).getAsJsonObject().get("service_id").getAsInt();
        }
        throw new IOException("Không có service nào available");
    }

    public String createOrder(Order order, OrderAddress address,
                              List<OrderItem> items) throws IOException {
        int totalWeight = items.stream()
                .mapToInt(i -> 500 * i.getQuantity())
                .sum();

        int fromDistrictId = 3695;
        int serviceId = getServiceId(fromDistrictId, address.getDistrictId());
        System.out.println("Using service_id: " + serviceId);

        StringBuilder itemsJson = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            itemsJson.append("{")
                    .append("\"name\":\"").append(escapeJson(item.getProduct().getName())).append("\",")
                    .append("\"quantity\":").append(item.getQuantity()).append(",")
                    .append("\"price\":").append((int) item.getPrice())
                    .append("}");
            if (i < items.size() - 1) itemsJson.append(",");
        }
        itemsJson.append("]");

        String body = "{"
                + "\"service_id\":" + serviceId + ","
                + "\"service_type_id\":2,"
                + "\"payment_type_id\":2,"
                + "\"required_note\":\"KHONGCHOXEMHANG\","
                + "\"to_name\":\"" + escapeJson(order.getReceiverName()) + "\","
                + "\"to_phone\":\"" + order.getReceiverPhone() + "\","
                + "\"to_address\":\"" + escapeJson(address.getAddress()) + "\","
                + "\"to_district_id\":" + address.getDistrictId() + ","
                + "\"to_ward_code\":\"" + address.getWardCode() + "\","
                + "\"weight\":" + totalWeight + ","
                + "\"cod_amount\":" + (int) order.getFinalAmount() + ","
                + "\"items\":" + itemsJson
                + "}";

        System.out.println("Create order body: " + body);
        JsonObject res = callPost("/v2/shipping-order/create", body);
        return res.getAsJsonObject("data").get("order_code").getAsString();
    }
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public boolean cancelOrder(String ghnOrderCode) throws IOException {
        String body = "{\"order_codes\":[\"" + ghnOrderCode + "\"]}";
        JsonObject res = callPost("/v2/switch-status/cancel", body);
        return res.get("code").getAsInt() == 200;
    }

    public String getOrderDetail(String ghnOrderCode) throws IOException {
        String body = "{\"order_code\":\"" + ghnOrderCode + "\"}";
        JsonObject res = callPost("/v2/shipping-order/detail", body);
        JsonObject data = res.getAsJsonObject("data");

        String status = data.get("status").getAsString();
        String statusVi = mapGHNStatus(status);

        JsonObject result = new JsonObject();
        result.addProperty("status", status);
        result.addProperty("statusVi", statusVi);
        return result.toString();
    }

    private String mapGHNStatus(String status) {
        switch (status) {
            case "ready_to_pick": return "Chờ lấy hàng";
            case "picking": return "Đang lấy hàng";
            case "picked": return "Đã lấy hàng";
            case "storing": return "Đang lưu kho";
            case "transporting": return "Đang vận chuyển";
            case "delivering": return "Đang giao hàng";
            case "delivered": return "Đã giao thành công";
            case "delivery_fail": return "Giao hàng thất bại";
            case "return": return "Đang hoàn hàng";
            case "returned": return "Đã hoàn hàng";
            case "cancel": return "Đã hủy";
            default: return status;
        }
    }

    public String getLeadTime(int toDistrictId, String toWardCode, int serviceId) throws IOException {
        String body = "{"
                + "\"to_district_id\":" + toDistrictId + ","
                + "\"to_ward_code\":\"" + toWardCode + "\","
                + "\"service_id\":" + serviceId
                + "}";

        JsonObject res = callPost("/v2/shipping-order/leadtime", body);
        JsonObject data = res.getAsJsonObject("data");

        long leadtimeTimestamp = data.get("leadtime").getAsLong();

        LocalDate date = Instant
                .ofEpochSecond(leadtimeTimestamp)
                .atZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"))
                .toLocalDate();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return date.format(formatter);
    }
}
