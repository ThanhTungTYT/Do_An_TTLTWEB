package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.model.cart.CartItem;

public class CartDao extends BaseDao {

    public void addItem(int userId, int productId, int quantity, double price) {
        getJdbi().useHandle(h ->
                h.createUpdate(
                                "INSERT INTO cart_items (user_id, product_id, quantity, price) " +
                                        "VALUES (:uid, :pid, :qty, :price) " +
                                        "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)"
                        )
                        .bind("uid", userId)
                        .bind("pid", productId)
                        .bind("qty", quantity)
                        .bind("price", price)
                        .execute()
        );
    }

    public void setQuantity(int userId, int productId, int quantity) {
        getJdbi().useHandle(h ->
                h.createUpdate(
                                "UPDATE cart_items SET quantity = :qty WHERE user_id = :uid AND product_id = :pid"
                        )
                        .bind("qty", quantity)
                        .bind("uid", userId)
                        .bind("pid", productId)
                        .execute()
        );
    }

    public void removeItem(int userId, int productId) {
        getJdbi().useHandle(h ->
                h.createUpdate(
                                "DELETE FROM cart_items WHERE user_id = :uid AND product_id = :pid"
                        )
                        .bind("uid", userId)
                        .bind("pid", productId)
                        .execute()
        );
    }

    public void clearCart(int userId) {
        getJdbi().useHandle(h ->
                h.createUpdate("DELETE FROM cart_items WHERE user_id = :uid")
                        .bind("uid", userId)
                        .execute()
        );
    }

    public Cart loadCart(int userId) {
        return getJdbi().withHandle(h -> {
            Cart cart = new Cart();
            h.createQuery(
                            "SELECT p.id, p.name, p.price, p.weight_grams, p.category_id, p.stock, p.state, " +
                                    "ci.quantity, ci.price AS cart_price, " +
                                    "(SELECT pi.image_url FROM product_images pi WHERE pi.product_id = p.id ORDER BY pi.id LIMIT 1) AS image_url " +
                                    "FROM cart_items ci " +
                                    "JOIN products p ON ci.product_id = p.id " +
                                    "WHERE ci.user_id = :uid"
                    )
                    .bind("uid", userId)
                    .map((rs, ctx) -> {
                        Product product = new Product();
                        product.setId(rs.getInt("id"));
                        product.setName(rs.getString("name"));
                        product.setPrice(rs.getDouble("price"));
                        product.setWeight_grams(rs.getInt("weight_grams"));
                        product.setCategory_id(rs.getInt("category_id"));
                        product.setStock(rs.getInt("stock"));
                        product.setState(rs.getString("state"));
                        product.setImage_url(rs.getString("image_url"));

                        return new CartItem(product, rs.getInt("quantity"), rs.getDouble("cart_price"));
                    })
                    .forEach(item -> cart.addItemDirectly(item));
            return cart;
        });
    }
}
