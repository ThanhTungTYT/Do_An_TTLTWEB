package com.example.do_an_ttltweb.model;
import java.sql.Timestamp;

public class User {
    private int id;
    private String full_name;
    private String email;
    private String phone;
    private String password_hash;
    private String role;
    private Timestamp createdAt;
    private String status;

    public User(int id, String full_name, String email, String phone, String password_hash, String role, String status) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.password_hash = password_hash;
        this.role = role;
        this.status = status;
    }

    public User() {}

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

