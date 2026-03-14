package com.example.do_an_ttltweb.model;

import java.sql.Timestamp;

public class Contact {
    private int id;
    private int user_id;
    private String full_name;
    private String email;
    private String message;
    private Timestamp sent_at;

    public Contact() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getSent_at() { return sent_at; }
    public void setSent_at(Timestamp sent_at) { this.sent_at = sent_at; }
}