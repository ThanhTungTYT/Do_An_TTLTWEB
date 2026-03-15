package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.CategoryDao;
import com.example.do_an_ttltweb.model.Category;

import java.util.List;

public class CategoryService {

    private CategoryDao categoryDao = new CategoryDao();

    public List<Category> getAllCategories(){
        return categoryDao.getAllCategories();
    }
    public boolean deleteCategory(int id) {
        return categoryDao.deleteCategory(id);
    }

    public boolean insertCategory(String name) {
        return categoryDao.insertCategory(name);
    }
}