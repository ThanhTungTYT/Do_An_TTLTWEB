package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.helper.base.BaseDao;

public class AuthDao extends BaseDao {

    public User findByEmail(String email) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = :e AND status = 'active'")
                        .bind("e", email)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean exists(String email) {
        Integer count = getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM users WHERE email = :e")
                        .bind("e", email)
                        .mapTo(Integer.class)
                        .one()
        );
        return count != null && count > 0;
    }

    public boolean register(User user) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO users(full_name, email, phone, password_hash, role, created_at, status) " +
                                        "VALUES (:fullname, :email, :phone, :pass, 'Customer', NOW(), 'active')"
                        )
                        .bind("fullname", user.getFull_name())
                        .bind("email", user.getEmail())
                        .bind("phone", user.getPhone())
                        .bind("pass", user.getPassword_hash())
                        .execute() > 0
        );
    }

    public boolean updatePassword(String email, String newPasswordHash) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE users SET password_hash = :pass WHERE email = :email")
                        .bind("pass", newPasswordHash)
                        .bind("email", email)
                        .execute() > 0
        );
    }

    public String getPasswordHashById(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT password_hash FROM users WHERE id = :id")
                        .bind("id", userId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean updatePasswordById(int userId, String newPasswordHash) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE users SET password_hash = :pass WHERE id = :id")
                        .bind("pass", newPasswordHash)
                        .bind("id", userId)
                        .execute() > 0
        );
    }
}