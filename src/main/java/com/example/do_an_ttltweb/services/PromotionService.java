package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.PromotionDao;
import com.example.do_an_ttltweb.model.Promotion;

import java.util.List;

public class PromotionService {
    private static PromotionService instance;
    private final PromotionDao promotionDao;

    private PromotionService() {
        promotionDao = new PromotionDao();
    }

    public static PromotionService getInstance() {
        if (instance == null) {
            instance = new PromotionService();
        }
        return instance;
    }

    public List<Promotion> getAllPromotions() {
        return promotionDao.getAll();
    }

    public List<Promotion> searchPromotions(String keyword) {
        return promotionDao.searchPromotions(keyword);
    }

    public int deletePromotionSafe(int id) {
        boolean isUsed = promotionDao.checkCodeUsed(id);

        if (isUsed) {
            return promotionDao.softDelete(id) > 0 ? 2 : 0;
        } else {
            return promotionDao.delete(id) > 0 ? 1 : 0;
        }
    }

    public boolean isCodeExist(String code) {
        return promotionDao.checkCodeExist(code);
    }

    public boolean addPromotion(Promotion p) {
        return promotionDao.insert(p) > 0;
    }

    public void autoUpdateStatus() {
        promotionDao.autoUpdateStates();
    }

    public boolean updatePromotion(Promotion p) {
        return promotionDao.update(p) > 0;
    }
    // Dùng cho trang thanh toán
    public List<Promotion> getAvailablePromotions() {
        return promotionDao.getAvailablePromotions();
    }

    // Dùng khi user chọn mã
    public Promotion getPromotionById(int id) {
        return promotionDao.getById(id);
    }
    // khong ap dung ma
    public Promotion getPromotionByCode(String code) {
        return promotionDao.getPromotionByCode(code);
    }

    public Promotion getNoPromo() {
        return getPromotionByCode("NO_PROMO");
    }
}