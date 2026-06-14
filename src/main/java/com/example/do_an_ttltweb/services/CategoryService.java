package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.CategoryDao;
import com.example.do_an_ttltweb.model.Category;

import java.util.List;

public class CategoryService {

    private CategoryDao categoryDao = new CategoryDao();

    public List<Category> getAllCategories(){
        return categoryDao.getAllCategories();
    }

    public boolean insertCategory(String name) {
        return categoryDao.insertCategory(name);
    }

    public List<Category> getActiveCategories() {
        return categoryDao.getActiveCategories();
    }
    public void updateCategory(int id,String name, String state) {
        categoryDao.updateCategory(id,name, state);
    }
}