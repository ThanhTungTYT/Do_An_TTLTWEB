package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.Banner;
import com.example.do_an_ttltweb.helper.base.BaseDao;
import java.util.List;

public class BannerDao extends BaseDao {

    public List<Banner> getAllBanners() { // Đã sửa tên hàm
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM banners")
                        .mapToBean(Banner.class)
                        .list()
        );
    }

    public List<Banner> getBannerActive() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM banners WHERE start_date <= NOW() AND end_date >= NOW() AND status = 'active'")
                        .mapToBean(Banner.class)
                        .list()
        );
    }

    public boolean addBanner(Banner banner) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO banners(banner_url, status, start_date, end_date) VALUES(:banner_url, :status, :start, :end)")
                        .bind("banner_url", banner.getBanner_url())
                        .bind("status", banner.getStatus())
                        .bind("start", banner.getStart_date())
                        .bind("end", banner.getEnd_date())
                        .execute() > 0
        );
    }

    public boolean updateBanner(int bid, Banner banner) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE banners SET banner_url = :banner, status = :status, start_date = :start, end_date = :end WHERE id = :bid")
                        .bind("banner", banner.getBanner_url())
                        .bind("status", banner.getStatus())
                        .bind("start", banner.getStart_date())
                        .bind("end", banner.getEnd_date())
                        .bind("bid", bid)
                        .execute() > 0
        );
    }

    public boolean deleteBanner(int bid) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("DELETE FROM banners WHERE id = :bid")
                        .bind("bid", bid)
                        .execute() > 0
        );
    }
}