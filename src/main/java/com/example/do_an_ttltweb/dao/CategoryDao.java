package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.Category;
import com.example.do_an_ttltweb.helper.base.BaseDao;

import java.util.List;

public class CategoryDao extends BaseDao {

    public List<Category> getAllCategories(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM categories")
                        .mapToBean(Category.class)
                        .list()
        );
    }

    public boolean insertCategory(String name) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO categories (name) VALUES (:name)")
                        .bind("name", name)
                        .execute() > 0
        );
    }
    public List<Category> getActiveCategories() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM categories where state ='Active' ")
                        .mapToBean(Category.class)
                        .list()
        );
    }
    public void updateCategoryState(int id, String state) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("UPDATE categories SET state = :state WHERE id = :id")
                        .bind("state", state)
                        .bind("id", id)
                        .execute()
        );
    }
}