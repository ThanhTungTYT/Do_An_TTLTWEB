package com.example.do_an_ttltweb.model;

public class ProductImage {
    private int id;
    private int product_id;
    private String image_url;

    public ProductImage() {}

    public ProductImage(int id, int product_id, String image_url) {
        this.id = id;
        this.product_id = product_id;
        this.image_url = image_url;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }
}