package com.example.do_an_ttltweb.dao;

import com.example.ltwebnhom23.model.ProductImage;

import java.util.List;

public class ImageDao extends BaseDao{

    public List<ProductImage> getAllImageById(int pid){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM product_images WHERE product_id = :pid ORDER BY id ASC")
                        .bind("pid", pid)
                        .mapToBean(ProductImage.class)
                        .list()
        );
    }
}