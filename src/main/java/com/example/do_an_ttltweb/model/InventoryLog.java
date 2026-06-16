package com.example.do_an_ttltweb.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class InventoryLog implements Serializable {
    private int id;
    private int product_id;
    private String product_name;
    private int quantity;
    private String action_type;
    private Timestamp created_at;

    public InventoryLog() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getAction_type() { return action_type; }
    public void setAction_type(String action_type) { this.action_type = action_type; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
}
