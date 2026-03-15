package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.ImageDao;
import com.example.do_an_ttltweb.model.ProductImage;

import java.util.List;

public class ImageService {

    private ImageDao imageDao = new ImageDao();

    public List<ProductImage> getAllImageById(int pid){
        return imageDao.getAllImageById(pid);
    }
}