package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.ProductImage;
import com.example.do_an_ttltweb.helper.base.BaseDao;

import java.util.List;

public class ImageDao extends BaseDao{

    public List<ProductImage> getAllImageById(int pid){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM product_images WHERE product_id = :pid ORDER BY position ASC")
                        .bind("pid", pid)
                        .mapToBean(ProductImage.class)
                        .list()
        );
    }

    public ProductImage getImageByPosition(int productId, int position) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM product_images WHERE product_id = :pid AND position = :pos LIMIT 1")
                        .bind("pid", productId)
                        .bind("pos", position)
                        .mapToBean(ProductImage.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public void updateImageUrl(int imageId, String newUrl) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("UPDATE product_images SET image_url = :url WHERE id = :id")
                        .bind("url", newUrl)
                        .bind("id", imageId)
                        .execute()
        );
    }
}