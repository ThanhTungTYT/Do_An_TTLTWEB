package com.example.do_an_ttltweb.model;

public class Address {
    private int id;
    private int user_id;
    private String country;
    private String province;
    private String ward;
    private String address;

    public Address() {
    }

    public Address(int id, int user_id, String country, String province, String ward, String address) {
        this.id = id;
        this.user_id = user_id;
        this.country = country;
        this.province = province;
        this.ward = ward;
        this.address = address;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getWard() {
        return ward;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
