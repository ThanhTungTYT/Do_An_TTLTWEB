package com.example.do_an_ttltweb.model;

import java.sql.Timestamp;

public class Banner {
    private int id;
    private String banner_url;
    private String status;
    private Timestamp start_date;
    private Timestamp end_date;

    public Banner(int id, String banner_url, String status, Timestamp start_date, Timestamp end_date) {
        this.id = id;
        this.banner_url = banner_url;
        this.status = status;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Banner() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }
    public void setStartDateFromForm(String start) {
        this.start_date = Timestamp.valueOf(start + " 00:00:00");
    }

    public void setEndDateFromForm(String end) {
        this.end_date = Timestamp.valueOf(end + " 23:59:59");
    }
}
