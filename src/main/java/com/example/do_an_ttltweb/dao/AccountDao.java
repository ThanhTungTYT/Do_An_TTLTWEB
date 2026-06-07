package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;
import com.example.do_an_ttltweb.model.*;

import java.sql.Timestamp;
import java.util.List;

public class AccountDao extends BaseDao {

    public User getUserById(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT id, full_name, email, phone, password_hash, role, created_at, status FROM users WHERE id = :id")
                        .bind("id", userId)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public Address getAddressByUserId(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT id, user_id, country, province, district, ward, address FROM user_addresses WHERE user_id = :uid")
                        .bind("uid", userId)
                        .mapToBean(Address.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public boolean updateUser(int userId, String fullName, String phone, int addressId,
                              String province, String district, String ward, String streetAddress) {
        return getJdbi().withHandle(handle -> {
            int userRows = handle.createUpdate("UPDATE users SET full_name = :name, phone = :phone WHERE id = :id")
                    .bind("name", fullName)
                    .bind("phone", phone)
                    .bind("id", userId)
                    .execute();

            int addrRows = 0;
            if (addressId > 0) {
                addrRows = handle.createUpdate("UPDATE user_addresses SET province = :province, district = :district, ward = :ward, address = :addr WHERE id = :aid")
                        .bind("province", province)
                        .bind("district", district)
                        .bind("ward", ward)
                        .bind("addr", streetAddress)
                        .bind("aid", addressId)
                        .execute();
            } else {
                addrRows = handle.createUpdate("INSERT INTO user_addresses (user_id, country, province, district, ward, address) VALUES (:uid, 'VN', :province, :district, :ward, :addr)")
                        .bind("uid", userId)
                        .bind("province", province)
                        .bind("district", district)
                        .bind("ward", ward)
                        .bind("addr", streetAddress)
                        .execute();
            }

            return userRows > 0 || addrRows > 0;
        });
    }
    public List<User> getAllUser(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users")
                        .mapToBean(User.class)
                        .list()
        );
    }
    public List<User> getNewUser(){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE DATE(created_at) = CURDATE()")
                        .mapToBean(User.class)
                        .list()
        );
    }
    public boolean banUser(int uid){
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE users SET status = 'ban' WHERE id = :uid")
                        .bind("uid", uid)
                        .execute() > 0
        );
    }
    public boolean unBanUser(int uid){
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE users SET status = 'active' WHERE id = :uid")
                        .bind("uid", uid)
                        .execute() > 0
        );
    }
    public boolean addUser(User user){
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO users(full_name, email, phone, password_hash, role, created_at, status) VALUES (:name, :email, :phone, :pass, :role, NOW(), 'active')")
                        .bind("name", user.getFull_name())
                        .bind("email", user.getEmail())
                        .bind("phone", user.getPhone())
                        .bind("pass", user.getPassword_hash())
                        .bind("role", user.getRole())
                        .execute() > 0
        );
    }
    public String getFullNameById(int userId) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT full_name FROM users WHERE id = :id")
                        .bind("id", userId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse("Không xác định")
        );
    }
    public List<User> getUserByKeyword(String key){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE full_name LIKE :kw OR email LIKE :kw OR phone LIKE :kw")
                        .bind("kw", "%" + key + "%")
                        .mapToBean(User.class)
                        .list()
        );
    }

    public boolean updateUser(int uid, User user){
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE users SET role = :role WHERE id = :uid")
                        .bind("role", user.getRole())
                        .bind("uid", uid)
                        .execute() > 0
        );
    }
    public int countNewCustomers(Timestamp start, Timestamp end) {
        return getJdbi().withHandle(h ->
                h.createQuery(
                                "SELECT COUNT(*) FROM users " +
                                        "WHERE created_at BETWEEN :start AND :end"
                        )
                        .bind("start", start)
                        .bind("end", end)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}