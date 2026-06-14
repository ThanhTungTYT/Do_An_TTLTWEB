package com.example.do_an_ttltweb.model;

import java.io.Serializable;

public class OrderAddress implements Serializable {
    int orderId;
    String country;
    String province;
    String ward;
    String address;
    private String district;
    private int districtId;
    private String wardCode;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public int getDistrictId() { return districtId; }
    public void setDistrictId(int districtId) { this.districtId = districtId; }
    public String getWardCode() { return wardCode; }
    public void setWardCode(String wardCode) { this.wardCode = wardCode; }
}
