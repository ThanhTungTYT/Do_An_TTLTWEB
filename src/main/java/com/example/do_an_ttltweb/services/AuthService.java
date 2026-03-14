package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.helper.hash.MD5Util;

public class AuthService {
    private final AuthDao authDao = new AuthDao();

    public User login(String email, String password) {
        User u = authDao.findByEmail(email);

        if (u != null && MD5Util.md5(password).equals(u.getPassword_hash())) {
            return u;
        }
        return null;
    }

    public boolean existsByEmail(String email) {
        return authDao.exists(email);
    }

    public boolean register(User user) {
        user.setPassword_hash(MD5Util.md5(user.getPassword_hash()));
        return authDao.register(user);
    }

    public boolean resetPassword(String email, String newRawPassword) {
        return authDao.updatePassword(email, MD5Util.md5(newRawPassword));
    }

    public boolean changePassword(int userId, String oldPassRaw, String newPassRaw) {
        String currentHashInDb = authDao.getPasswordHashById(userId);
        if (currentHashInDb == null) return false;

        if (!MD5Util.md5(oldPassRaw).equals(currentHashInDb)) {
            return false;
        }

        return authDao.updatePasswordById(userId, MD5Util.md5(newPassRaw));
    }
}