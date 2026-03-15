package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.Category;
import com.example.do_an_ttltweb.helper.base.BaseDao;

import java.util.List;

public class CategoryDao extends BaseDao {

    public List<Category> getAllCategories(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM categories where state ='active' ")
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
    public boolean deleteCategory(int id) {
        return getJdbi().inTransaction(handle -> {
            handle.createUpdate("UPDATE products SET state = 'Deleted' WHERE category_id = :id")
                    .bind("id", id)
                    .execute();
            return handle.createUpdate("UPDATE categories SET state = 'Deleted' WHERE id = :id")
                    .bind("id", id)
                    .execute() > 0;
        });
    }
}