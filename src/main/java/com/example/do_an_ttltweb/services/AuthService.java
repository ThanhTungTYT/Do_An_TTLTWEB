package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.helper.hash.MD5Util;
import java.util.*;

public class AuthService {
    private final AuthDao authDao = new AuthDao();
    private static final int MAX_ATTEMPTS = 5;

    public User login(String email, String password) {
        User u = authDao.findByEmail(email);

        if (u != null && MD5Util.md5(password).equals(u.getPassword_hash())) {
            return u;
        }
        return null;
    }

    public boolean isBan(String email){
        User u = authDao.isBan(email);

        if(u != null){
            return true;
        }
        return false;
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

    public void loadUserPermissions(User user) {
        if (user == null) return;
        AuthDao dao = new AuthDao();
        List<String> perms = dao.getPermissionsByUserId(user.getId());

        if (!perms.contains("shopping")) perms.add("shopping");

        user.setPermissions(perms);
    }

    public boolean isLocked(String email) {
        return authDao.countFailedAttempts(email) >= MAX_ATTEMPTS;
    }

    public void recordFailed(String email) {
        authDao.recordFailedAttempt(email);
    }

    public void clearFailed(String email) {
        authDao.clearFailedAttempts(email);
    }

    public int getRemainingAttempts(String email) {
        return MAX_ATTEMPTS - authDao.countFailedAttempts(email);
    }
}