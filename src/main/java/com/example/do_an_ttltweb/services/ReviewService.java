package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.OrderDao;
import com.example.do_an_ttltweb.dao.ReviewDao;
import com.example.do_an_ttltweb.model.ProductReview;

import java.util.List;

public class ReviewService {

    private ReviewDao reviewDao = new ReviewDao();
    private OrderDao orderDao = new OrderDao();

    public boolean addReview(ProductReview review){
        return reviewDao.addReview(review);
    }

    public List<ProductReview> getReviewForProduct(int pid){
        return reviewDao.getReviewForProduct(pid);
    }

    public List<ProductReview> getAllReview(){
        return reviewDao.getAllReview();
    }

    public boolean deleteReview(int rid){
        return reviewDao.deleteReview(rid);
    }

    public List<ProductReview> getReviewByKey(String key){
        return reviewDao.getReviewByKey(key);
    }

    public List<ProductReview> getReviewByTime(String start, String end){
        return reviewDao.getReviewByTime(start, end);
    }
    public boolean isBuy(int uid, int pid){
        int buy = orderDao.getOrderByCondition(pid, uid).size();

        if(buy > 0){
            return true;
        }

        return false;
    }
    public boolean isSpam(int uid, int pid){
        int spam = reviewDao.getCountInMinute(uid, pid);

        if(spam > 3){
            return true;
        }
        return false;
    }
}
