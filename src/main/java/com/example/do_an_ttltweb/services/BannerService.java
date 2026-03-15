package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.BannerDao;
import com.example.do_an_ttltweb.model.Banner;
import java.util.List;

public class BannerService {

    private final BannerDao bannerDao = new BannerDao(); // Đổi tên biến cho rõ nghĩa

    public List<Banner> getAllBanners() {
        return bannerDao.getAllBanners();
    }

    public List<Banner> getBannerActive() {
        return bannerDao.getBannerActive();
    }

    public boolean addBanner(Banner b) {
        return bannerDao.addBanner(b);
    }

    public boolean updateBanner(int bid, Banner b) {
        return bannerDao.updateBanner(bid, b);
    }

    public boolean deleteBanner(int bid) {
        return bannerDao.deleteBanner(bid);
    }
}