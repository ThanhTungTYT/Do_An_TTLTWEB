package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;
import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.model.cart.CartItem;
import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.OrderAddress;
import com.example.do_an_ttltweb.model.OrderItem;
import com.example.do_an_ttltweb.model.Product;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDao extends BaseDao {

    public boolean createOrder(Order order, OrderAddress address, Cart checkoutCart) {
        return getJdbi().inTransaction(h -> {
            try {
                int orderId = h.createUpdate(
                                "INSERT INTO orders (" +
                                        "user_id, payment_method_id, promo_id, " +
                                        "receiver_name, receiver_phone, note, " +
                                        "total_amount, shipping_fee, discount_percent, final_amount, created_at, status" +
                                        ") VALUES (" +
                                        ":userId, :paymentMethodId, :promoId, " +
                                        ":receiverName, :receiverPhone, :note, " +
                                        ":totalAmount, :shippingFee, :discountPercent, :finalAmount, :createdAt, :status)"
                        ).bindBean(order)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class).one();

                order.setId(orderId);
                h.createUpdate(
                                "INSERT INTO order_addresses (order_id, country, province, district, ward, address, district_id, ward_code) " +
                                        "VALUES (:orderId, :country, :province, :district, :ward, :address, :districtId, :wardCode)")
                        .bind("orderId", orderId)
                        .bind("country", address.getCountry())
                        .bind("province", address.getProvince())
                        .bind("district", address.getDistrict())
                        .bind("ward", address.getWard())
                        .bind("address", address.getAddress())
                        .bind("districtId", address.getDistrictId())
                        .bind("wardCode", address.getWardCode())
                        .execute();

                var batch = h.prepareBatch(
                        "INSERT INTO order_items(order_id, product_id, price, quantity) " +
                                "VALUES (:orderId, :productId, :price, :quantity)"
                );

                for (CartItem i : checkoutCart.getList()) {

                    batch.bind("orderId", orderId)
                            .bind("productId", i.getProduct().getId())
                            .bind("price", i.getPrice())
                            .bind("quantity", i.getQuantity())
                            .add();

                    int updateProduct = h.createUpdate(
                                    "UPDATE products " +
                                            "SET stock = stock - :qty, sold = sold + :qty " +
                                            "WHERE id = :pid AND stock >= :qty AND state = 'active'")
                            .bind("qty", i.getQuantity())
                            .bind("pid", i.getProduct().getId())
                            .execute();

                    if (updateProduct == 0) {
                        throw new RuntimeException("Sản phẩm " + i.getProduct().getName() + " đã hết hàng hoặc không đủ số lượng.");
                    }

                    h.createUpdate("UPDATE products SET state = 'inactive' WHERE id = :pid AND stock <= 0")
                            .bind("pid", i.getProduct().getId())
                            .execute();
                }
                batch.execute();

                if (order.getPromoId() != null && order.getPromoId() > 0) {
                    h.createUpdate(
                            "UPDATE promotions " +
                                    "SET quantity = quantity - 1 " +
                                    "WHERE id = :pid AND quantity > 0 AND state = 'active'"
                    ).bind("pid", order.getPromoId()).execute();
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        });
    }
    public List<Order> getAllOrders() {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT id, user_id, payment_method_id, promo_id, " +
                                        "receiver_name, receiver_phone, note, " +
                                        "total_amount, shipping_fee, discount_percent, final_amount, " +
                                        "status, created_at " +
                                        "FROM orders " +
                                        "ORDER BY created_at DESC"
                        )
                        .mapToBean(Order.class)
                        .list()
        );
    }
    public List<Order> getOrdersByUserId(int userId) {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT id, user_id, payment_method_id, promo_id, " +
                                        "receiver_name, receiver_phone, note, " +
                                        "total_amount, shipping_fee, discount_percent, final_amount, " +
                                        "status, created_at, ghn_order_code " +
                                        "FROM orders " +
                                        "WHERE user_id = :uid " +
                                        "ORDER BY created_at DESC"
                        )
                        .bind("uid", userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public List<OrderItem> getItemsByOrderId(int orderId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT " +
                                        " oi.id, oi.order_id, oi.product_id, oi.quantity, oi.price, " +
                                        " p.name AS product_name, " +
                                        " ( " +
                                        "   SELECT pi.image_url " +
                                        "   FROM product_images pi " +
                                        "   WHERE pi.product_id = p.id AND pi.position = 0 " +
                                        "   LIMIT 1 " +
                                        " ) AS image_url " +
                                        "FROM order_items oi " +
                                        "JOIN products p ON oi.product_id = p.id " +
                                        "WHERE oi.order_id = :oid"
                        )
                        .bind("oid", orderId)
                        .map((rs, ctx) -> {
                            Product product = new Product();
                            product.setId(rs.getInt("product_id"));
                            product.setName(rs.getString("product_name"));
                            product.setImage_url(rs.getString("image_url"));

                            OrderItem item = new OrderItem();
                            item.setId(rs.getInt("id"));
                            item.setOrderId(rs.getInt("order_id"));
                            item.setProductId(rs.getInt("product_id"));
                            item.setQuantity(rs.getInt("quantity"));
                            item.setPrice(rs.getDouble("price"));
                            item.setProduct(product);

                            return item;
                        })
                        .list()
        );
    }
    public boolean updateOrderStatus(Order order) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate(
                                "UPDATE orders " +
                                        "SET status = :status " +
                                        "WHERE id = :orderId"
                        )
                        .bind("status", order.getStatus())
                        .bind("orderId", order.getId())
                        .execute() > 0
        );
    }
    public boolean cancelOrder(Order order, int userId) {
        return getJdbi().inTransaction(handle -> {

            int updateOrder = handle.createUpdate(
                            "UPDATE orders SET status = :status WHERE id = :id AND user_id = :userId"
                    )
                    .bind("status", "Đã hủy")
                    .bind("id", order.getId())
                    .bind("userId", userId)
                    .execute();

            if (updateOrder == 0) {
                throw new RuntimeException("Không tìm thấy đơn hàng");
            }

            List<OrderItem> items = handle.createQuery(
                            "SELECT product_id, quantity FROM order_items WHERE order_id = :oid"
                    )
                    .bind("oid", order.getId())
                    .mapToBean(OrderItem.class)
                    .list();

            for (OrderItem item : items) {
                handle.createUpdate("UPDATE products " +
                                "SET stock = stock + :qty, sold = sold - :qty " +
                                "WHERE id = :pid"
                        )
                        .bind("qty", item.getQuantity())
                        .bind("pid", item.getProductId())
                        .execute();

                handle.createUpdate("UPDATE products SET state = 'active' WHERE id = :pid AND stock > 0")
                        .bind("pid", item.getProductId())
                        .execute();
            }

            Integer promoId = handle.createQuery("SELECT promo_id FROM orders WHERE id = :oid")
                    .bind("oid", order.getId())
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (promoId != null) {
                handle.createUpdate(
                                "UPDATE promotions SET quantity = quantity + 1 WHERE id = :pid"
                        )
                        .bind("pid", promoId)
                        .execute();

                handle.createUpdate(
                                "UPDATE promotions SET state = 'active' WHERE id = :pid AND quantity > 0"
                        )
                        .bind("pid", promoId)
                        .execute();
            }
            return true;
        });
    }
    public double getTotalRevenue(Timestamp start, Timestamp end) {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT COALESCE(SUM(final_amount),0) " +
                                        "FROM orders " +
                                        "WHERE status = 'Đã giao' " +
                                        "AND created_at BETWEEN :start AND :end"
                        )
                        .bind("start", start)
                        .bind("end", end)
                        .mapTo(Double.class)
                        .one()
        );
    }

    public int countOrders(Timestamp start, Timestamp end) {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT COUNT(*) FROM orders " +
                                        "WHERE created_at BETWEEN :start AND :end"
                        )
                        .bind("start", start)
                        .bind("end", end)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countPendingOrders(Timestamp start, Timestamp end) {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT COUNT(*) FROM orders " +
                                        "WHERE status = 'Đang xử lý' " +
                                        "AND created_at BETWEEN :start AND :end"
                        )
                        .bind("start", start)
                        .bind("end", end)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<Map<String, Object>> getTopProducts(Timestamp start, Timestamp end) {

        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT " +
                                        "p.id AS productId, " +
                                        "p.name AS productName, " +
                                        "SUM(oi.quantity) AS totalSold " +
                                        "FROM order_items oi " +
                                        "JOIN orders o ON oi.order_id = o.id " +
                                        "JOIN products p ON oi.product_id = p.id " +
                                        "WHERE o.status = 'Đã giao' " +
                                        "AND o.created_at BETWEEN :start AND :end " +
                                        "GROUP BY p.id, p.name " +
                                        "ORDER BY totalSold DESC " +
                                        "LIMIT 5"
                        )
                        .bind("start", start)
                        .bind("end", end)
                        .map((rs, ctx) -> {

                            Product product = new Product();
                            product.setId(rs.getInt("productId"));
                            product.setName(rs.getString("productName"));

                            Map<String, Object> row = new HashMap<>();
                            row.put("product", product);
                            row.put("totalSold", rs.getInt("totalSold"));

                            return row;
                        })
                        .list()
        );
    }

    public List<Order> getOrdersByDate(Timestamp start, Timestamp end) {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT * FROM orders " +
                                        "WHERE created_at BETWEEN :start AND :end " +
                                        "ORDER BY created_at DESC"
                        )
                        .bind("start", start)
                        .bind("end", end)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public List<Order> getOrderByCondition(int pid, int uid){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT o.* FROM orders o JOIN order_items oi ON o.id = oi.order_id WHERE o.user_id = :uid AND oi.product_id = :pid ")
                        .bind("uid", uid)
                        .bind("pid", pid)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public int countOrdersWithFilter(String startDate, String endDate, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders WHERE 1=1 ");

        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND DATE(created_at) >= :start ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND DATE(created_at) <= :end ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append("AND status = :status ");
        }

        return getJdbi().withHandle(h -> {
            var query = h.createQuery(sql.toString());
            if (startDate != null && !startDate.isEmpty()) query.bind("start", startDate);
            if (endDate != null && !endDate.isEmpty()) query.bind("end", endDate);
            if (status != null && !status.isEmpty()) query.bind("status", status);
            return query.mapTo(Integer.class).one();
        });
    }

    public List<Order> getOrdersWithFilter(String startDate, String endDate,String status, int limit, int offset) {
        StringBuilder sql = new StringBuilder(
                "SELECT id, user_id, payment_method_id, promo_id, " +
                        "receiver_name, receiver_phone, note, " +
                        "total_amount, shipping_fee, discount_percent, final_amount, " +
                        "status, created_at, ghn_order_code " +
                        "FROM orders WHERE 1=1 "
        );

        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND DATE(created_at) >= :start ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND DATE(created_at) <= :end ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append("AND status = :status ");
        }

        sql.append("ORDER BY created_at DESC LIMIT :limit OFFSET :offset");

        return getJdbi().withHandle(h -> {
            var query = h.createQuery(sql.toString());
            if (startDate != null && !startDate.isEmpty()) query.bind("start", startDate);
            if (endDate != null && !endDate.isEmpty()) query.bind("end", endDate);
            if (status != null && !status.isEmpty()) query.bind("status", status);

            query.bind("limit", limit);
            query.bind("offset", offset);

            return query.mapToBean(Order.class).list();
        });
    }

    public List<Order> searchOrders(String keyword) {
        String sql = "SELECT o.* " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.id " +
                "WHERE o.id LIKE :key " +
                "OR u.full_name LIKE :key " +
                "ORDER BY o.created_at DESC";

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("key", "%" + keyword + "%")
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public Order getOrderById(int orderId) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT * FROM orders WHERE id = :id")
                        .bind("id", orderId)
                        .mapToBean(Order.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public OrderAddress getAddressByOrderId(int orderId) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT * FROM order_addresses WHERE order_id = :oid")
                        .bind("oid", orderId)
                        .mapToBean(OrderAddress.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public boolean updateOrderStatusAndGhn(int orderId, String status, String ghnCode) {
        return getJdbi().withHandle(h ->
                h.createUpdate(
                                "UPDATE orders SET status = :status, ghn_order_code = :ghnCode WHERE id = :id")
                        .bind("status", status)
                        .bind("ghnCode", ghnCode)
                        .bind("id", orderId)
                        .execute() > 0
        );
    }
    public boolean updateOrderStatusById(int orderId, String status) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate(
                                "UPDATE orders " +
                                        "SET status = :status " +
                                        "WHERE id = :orderId"
                        )
                        .bind("status", status)
                        .bind("orderId", orderId)
                        .execute() > 0
        );
    }

    public boolean adminCancelOrder(int orderId) {
        return getJdbi().inTransaction(handle -> {

            try {
                String ghnOrderCode = handle.createQuery("SELECT ghn_order_code FROM orders WHERE id = :id")
                        .bind("id", orderId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null);

                if (ghnOrderCode != null && !ghnOrderCode.trim().isEmpty()) {
                    com.example.do_an_ttltweb.services.GHNService ghnService = new com.example.do_an_ttltweb.services.GHNService();
                    ghnService.cancelOrder(ghnOrderCode);
                    System.out.println("Đã hủy thành công đơn hàng " + ghnOrderCode + " trên hệ thống GHN");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi gọi API hủy đơn của GHN: " + e.getMessage());
            }

            int updateOrder = handle.createUpdate(
                            "UPDATE orders SET status = 'Đã hủy' WHERE id = :id"
                    )
                    .bind("id", orderId)
                    .execute();

            if (updateOrder == 0) {
                throw new RuntimeException("Không tìm thấy đơn hàng hoặc đơn hàng đã bị thay đổi.");
            }

            List<OrderItem> items = handle.createQuery(
                            "SELECT product_id, quantity FROM order_items WHERE order_id = :oid"
                    )
                    .bind("oid", orderId)
                    .mapToBean(OrderItem.class)
                    .list();

            for (OrderItem item : items) {
                handle.createUpdate("UPDATE products " +
                                "SET stock = stock + :qty, sold = sold - :qty " +
                                "WHERE id = :pid"
                        )
                        .bind("qty", item.getQuantity())
                        .bind("pid", item.getProductId())
                        .execute();

                handle.createUpdate("UPDATE products SET state = 'active' WHERE id = :pid AND stock > 0")
                        .bind("pid", item.getProductId())
                        .execute();
            }

            Integer promoId = handle.createQuery("SELECT promo_id FROM orders WHERE id = :oid")
                    .bind("oid", orderId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (promoId != null) {
                handle.createUpdate(
                                "UPDATE promotions SET quantity = quantity + 1 WHERE id = :pid"
                        )
                        .bind("pid", promoId)
                        .execute();

                handle.createUpdate(
                                "UPDATE promotions SET state = 'active' WHERE id = :pid AND quantity > 0"
                        )
                        .bind("pid", promoId)
                        .execute();
            }
            return true;
        });
    }
}
