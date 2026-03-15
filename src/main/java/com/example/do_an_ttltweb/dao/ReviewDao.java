package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;
import com.example.do_an_ttltweb.model.ProductReview;

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
            handle.createQuery("SELECT r.*, u.full_name AS username FROM products_review r JOIN users u ON r.user_id = u.id WHERE product_id = :pid")
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

    public List<ProductReview> getReviewByTime(String start, String end){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT r.*, p.name AS productname, u.full_name AS username FROM products_review r JOIN products p ON r.product_id = p.id JOIN users u ON r.user_id = u.id WHERE DATE(r.created_at) >= :start AND DATE(r.created_at) <= :end ORDER BY r.created_at DESC")
                        .bind("start", start)
                        .bind("end", end)
                        .mapToBean(ProductReview.class)
                        .list()
        );
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


}
