package com.example.do_an_ttltweb.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Promotion implements Serializable {
    private int id;
    private String code;
    private String description;
    private double discountPercent;
    private double minOrderValue;
    private Timestamp startDate;
    private Timestamp endDate;
    private int quantity;
    private String state;

    public Promotion() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public double getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(double minOrderValue) { this.minOrderValue = minOrderValue; }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public Timestamp getEndDate() { return endDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getState() {return state; }
    public void setState(String state) {this.state = state; }
}