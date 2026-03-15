package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;
import com.example.do_an_ttltweb.model.Promotion;

import java.util.List;

public class PromotionDao extends BaseDao {

    public List<Promotion> getAll() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT id, code, description, " +
                                "discount_percent AS discountPercent, " +
                                "min_order_value AS minOrderValue, " +
                                "start_date AS startDate, " +
                                "end_date AS endDate, " +
                                "quantity, state " +
                                "FROM promotions ORDER BY id DESC")
                        .mapToBean(Promotion.class)
                        .list()
        );
    }

    public void autoUpdateStates() {
        getJdbi().useHandle(handle -> {
            String sqlInactive = "UPDATE promotions SET state = 'inactive' " +
                    "WHERE (start_date > NOW() OR end_date < NOW() OR quantity <= 0) " +
                    "AND state != 'inactive'";
            String sqlActive = "UPDATE promotions SET state = 'active' " +
                    "WHERE start_date <= NOW() AND end_date >= NOW() AND quantity > 0 " +
                    "AND state != 'active'";
            handle.createUpdate(sqlInactive).execute();
            handle.createUpdate(sqlActive).execute();
        });
    }

    public List<Promotion> searchPromotions(String keyword) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT id, code, description, " +
                                "discount_percent AS discountPercent, " +
                                "min_order_value AS minOrderValue, " +
                                "start_date AS startDate, " +
                                "end_date AS endDate, " +
                                "quantity " +
                                "FROM promotions " +
                                "WHERE code LIKE :key OR CAST(id AS CHAR) LIKE :key " +
                                "ORDER BY id DESC")
                        .bind("key", "%" + keyword + "%")
                        .mapToBean(Promotion.class)
                        .list()
        );
    }

    public int delete(int id) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("DELETE FROM promotions WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    public boolean checkCodeExist(String code) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM promotions WHERE code = :code")
                        .bind("code", code)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public int insert(Promotion p) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO promotions (code, description, discount_percent, min_order_value, quantity, start_date, end_date, state) " +
                                "VALUES (:code, :desc, :discount, :min, :qty, :start, :end, :state)")
                        .bind("code", p.getCode())
                        .bind("desc", p.getDescription())
                        .bind("discount", p.getDiscountPercent())
                        .bind("min", p.getMinOrderValue())
                        .bind("qty", p.getQuantity())
                        .bind("start", p.getStartDate())
                        .bind("end", p.getEndDate())
                        .bind("state", p.getState())
                        .execute()
        );
    }

    public int update(Promotion p) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE promotions SET description = :desc, discount_percent = :discount, " +
                                "min_order_value = :min, quantity = :qty, start_date = :start, end_date = :end, state = :state " +
                                "WHERE id = :id")
                        .bind("desc", p.getDescription())
                        .bind("discount", p.getDiscountPercent())
                        .bind("min", p.getMinOrderValue())
                        .bind("qty", p.getQuantity())
                        .bind("start", p.getStartDate())
                        .bind("end", p.getEndDate())
                        .bind("state", p.getState())
                        .bind("id", p.getId())
                        .execute()
        );
    }
    // ================= CHECKOUT =================

    // Lấy các mã hợp lệ cho thanh toán
    public List<Promotion> getAvailablePromotions() {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT id, code, description, " +
                                        "discount_percent AS discountPercent, " +
                                        "min_order_value AS minOrderValue, " +
                                        "start_date AS startDate, " +
                                        "end_date AS endDate, " +
                                        "quantity, state " +
                                        "FROM promotions " +
                                        "WHERE state = 'active' " +
                                        "AND quantity > 0 " +
                                        "AND start_date <= NOW() " +
                                        "AND end_date >= NOW()"
                        )
                        .mapToBean(Promotion.class)
                        .list()
        );
    }

    // Lấy 1 promotion theo id
    public Promotion getById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT id, code, description, " +
                                        "discount_percent AS discountPercent, " +
                                        "min_order_value AS minOrderValue, " +
                                        "start_date AS startDate, " +
                                        "end_date AS endDate, " +
                                        "quantity, state " +
                                        "FROM promotions WHERE id = :id"
                        )
                        .bind("id", id)
                        .mapToBean(Promotion.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public boolean checkCodeUsed(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM orders WHERE promo_id = :id")
                        .bind("id", id)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public int softDelete(int id) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE promotions SET quantity = 0, state = 'inactive' WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }
    public Promotion getPromotionByCode(String code) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM promotions  WHERE code = :code LIMIT 1")
                        .bind("code", code)
                        .mapToBean(Promotion.class)
                        .findOne()
                        .orElse(null)
        );
    }

}