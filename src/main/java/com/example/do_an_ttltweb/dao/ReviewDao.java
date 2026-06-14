package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;
import com.example.do_an_ttltweb.model.ProductReview;
import com.example.do_an_ttltweb.model.Promotion;

import java.util.List;

public class ReviewDao extends BaseDao {

    public boolean addReview(ProductReview review){
        return getJdbi().withHandle(handle ->
            handle.createUpdate(" INSERT INTO products_review( product_id, user_id, rating, comment, created_at) \n" +
                    "VALUES(:product_id, :user_id, :rating, :comment, NOW());")
                    .bind("product_id", review.getProductId())
                    .bind("user_id", review.getUserId())
                    .bind("rating", review.getRating())
                    .bind("comment", review.getComment())
                    .execute() > 0
        );
    }

    public List<ProductReview> getReviewForProduct(int pid){
        return getJdbi().withHandle(handle ->
            handle.createQuery("SELECT r.*, u.full_name AS username FROM products_review r JOIN users u ON r.user_id = u.id WHERE product_id = :pid ORDER BY r.created_at DESC")
                    .bind("pid", pid)
                    .mapToBean(ProductReview.class)
                    .list()
        );
    }

    public List<ProductReview> getAllReview(){
        return getJdbi().withHandle(handle ->
            handle.createQuery("SELECT r.*, p.name AS productname, u.full_name AS username FROM products_review r JOIN products p ON r.product_id = p.id JOIN users u ON r.user_id = u.id ORDER BY r.created_at DESC")
                    .mapToBean(ProductReview.class)
                    .list()
        );
    }

    public boolean deleteReview(int rid){
        return getJdbi().withHandle(handle ->
                handle.createUpdate("DELETE FROM products_review WHERE id = :rid")
                        .bind("rid", rid)
                        .execute() > 0
        );
    }

    public List<ProductReview> getReviewByKey(String key){
        return getJdbi().withHandle(handle ->
            handle.createQuery("SELECT r.*, p.name AS productname, u.full_name AS username FROM products_review r JOIN products p ON r.product_id = p.id JOIN users u ON r.user_id = u.id WHERE p.name LIKE :kw OR u.full_name LIKE :kw ORDER BY r.created_at DESC ")
                    .bind("kw", "%"+key+"%")
                    .mapToBean(ProductReview.class)
                    .list()
        );
    }

    public List<ProductReview> getReviewByTime(String start, String end) {
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, p.name AS productname, u.full_name AS username " +
                        "FROM products_review r " +
                        "JOIN products p ON r.product_id = p.id " +
                        "JOIN users u ON r.user_id = u.id " +
                        "WHERE 1=1 "
        );

        if (start != null && !start.isEmpty()) {
            sql.append("AND DATE(r.created_at) >= :start ");
        }
        if (end != null && !end.isEmpty()) {
            sql.append("AND DATE(r.created_at) <= :end ");
        }

        sql.append("ORDER BY r.created_at DESC");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (start != null && !start.isEmpty()) query.bind("start", start);
            if (end != null && !end.isEmpty()) query.bind("end", end);
            return query.mapToBean(ProductReview.class).list();
        });
    }

    public int getCountInMinute(int uid, int pid){
        return getJdbi().withHandle(handle ->
            handle.createQuery("SELECT COUNT(*) FROM products_review WHERE user_id = :uid AND product_id = :pid AND created_at >= NOW() - INTERVAL 1 MINUTE")
                    .bind("uid", uid)
                    .bind("pid", pid)
                    .mapTo(int.class)
                    .one()
        );
    }

    public int count() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM products_review")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<ProductReview> getPaginated(int limit, int offset) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT r.*, p.name AS productname, u.full_name AS username FROM products_review r JOIN products p ON r.product_id = p.id JOIN users u ON r.user_id = u.id ORDER BY r.created_at DESC LIMIT :limit OFFSET :offset")
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(ProductReview.class)
                        .list()
        );
    }
}
