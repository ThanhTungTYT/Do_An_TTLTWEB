package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.helper.base.BaseDao;
import java.util.*;

public class AuthDao extends BaseDao {

    public User findByEmail(String email) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = :e")
                        .bind("e", email)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public User isBan(String email) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = :e AND status = 'ban'")
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
                                        "VALUES (:fullname, :email, :phone, :pass, 'customer', NOW(), 'active')"
                        )
                        .bind("fullname", user.getFull_name())
                        .bind("email", user.getEmail())
                        .bind("phone", user.getPhone())
                        .bind("pass", user.getPassword_hash() == null ? "" : user.getPassword_hash())
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

    public List<String> getPermissionsByUserId(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.permission_key FROM permissions p " +
                                "JOIN user_permissions up ON p.id = up.permission_id " +
                                "WHERE up.user_id = :uid")
                        .bind("uid", userId)
                        .mapTo(String.class)
                        .list()
        );
    }

    public List<Map<String, Object>> getAllPermissions() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT id, permission_name, permission_key FROM permissions")
                        .mapToMap()
                        .list()
        );
    }

    public void updateUserPermissions(int userId, String[] permissionIds) {
        getJdbi().useHandle(handle -> {
            handle.useTransaction(h -> {
                h.createUpdate("DELETE FROM user_permissions WHERE user_id = :uid")
                        .bind("uid", userId).execute();
                if (permissionIds != null) {
                    for (String pId : permissionIds) {
                        h.createUpdate("INSERT INTO user_permissions(user_id, permission_id) VALUES (:uid, :pid)")
                                .bind("uid", userId).bind("pid", Integer.parseInt(pId)).execute();
                    }
                }
            });
        });
    }

    public int countFailedAttempts(String email) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM login_attempts " +
                                "WHERE email = :email AND attempt_time >= NOW() - INTERVAL 1 HOUR")
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void recordFailedAttempt(String email) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("INSERT INTO login_attempts(email) VALUES(:email)")
                        .bind("email", email)
                        .execute()
        );
    }

    public void clearFailedAttempts(String email) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("DELETE FROM login_attempts WHERE email = :email")
                        .bind("email", email)
                        .execute()
        );
    }
}