package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.AccountDao;
import com.example.do_an_ttltweb.dao.OrderDao;
import com.example.do_an_ttltweb.helper.hash.MD5Util;
import com.example.do_an_ttltweb.model.Address;
import com.example.do_an_ttltweb.model.User;

import java.util.List;

public class AccountService {
    private AccountDao accountDao = new AccountDao();
    private final OrderDao orderDao = new OrderDao();

    public User getAccountInfo(int userId) {
        return accountDao.getUserById(userId);
    }

    public Address getUserAddress(int userId) {
        return accountDao.getAddressByUserId(userId);
    }

    public boolean updateUserInfo(int userId, String fullName, String phone, int addressId, String city, String district, String streetAddress) {
        return accountDao.updateUser(userId, fullName, phone, addressId, city, district, streetAddress);
    }


    public List<User> getAllUser(){
        return accountDao.getAllUser();
    }
    public List<User> getNewUser(){
        return accountDao.getNewUser();
    }



    public boolean banUser(int uid){
        return accountDao.banUser(uid);
    }
    public boolean unBanUser(int uid){
        return accountDao.unBanUser(uid);
    }
    public boolean addUser(User user){
        String hashedPassword = MD5Util.md5(user.getPassword_hash());
        user.setPassword_hash(hashedPassword);

        return accountDao.addUser(user);
    }
    public List<User> getUserByKeyword(String key){
        return accountDao.getUserByKeyword(key);
    }
    public boolean updateUser(int uid, User user){
        return accountDao.updateUser(uid, user);
    }
}