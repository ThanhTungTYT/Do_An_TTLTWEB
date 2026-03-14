package com.example.do_an_ttltweb.model;

import java.sql.Timestamp;

public class Product {
    private int id;
    private int category_id;
    private String name;
    private String description;
    private int stock;
    private int sold;
    private int weight_grams;
    private double price;
    private Timestamp created_at;
    private String state;

    private String image_url;
    private String category_name;
    private double avg_rating;

    public Product() {}

    public Product(int id, int category_id, String name, String description, int stock, int sold, int weight_grams, double price, Timestamp created_at, String state) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.sold = sold;
        this.weight_grams = weight_grams;
        this.price = price;
        this.created_at = created_at;
        this.state = state;
    }

    // --- Getter & Setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategory_id() { return category_id; }
    public void setCategory_id(int category_id) { this.category_id = category_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getSold() { return sold; }
    public void setSold(int sold) { this.sold = sold; }

    public int getWeight_grams() { return weight_grams; }
    public void setWeight_grams(int weight_grams) { this.weight_grams = weight_grams; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getCategory_name() { return category_name; }
    public void setCategory_name(String category_name) { this.category_name = category_name; }

    public double getAvg_rating() { return avg_rating; }
    public void setAvg_rating(double avg_rating) { this.avg_rating = avg_rating; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}