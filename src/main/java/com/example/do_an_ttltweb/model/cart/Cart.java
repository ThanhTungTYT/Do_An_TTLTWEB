package com.example.do_an_ttltweb.model.cart;

import com.example.do_an_ttltweb.model.Product;

import java.io.Serializable;
import java.util.*;
public class Cart implements Serializable {
    private Map<Integer, CartItem> data;

    public Cart(){
        data = new HashMap<>();
    }

    public void addProduct(Product product, int quantity){
        if(data.containsKey(product.getId())) {
            data.get(product.getId()).upQuantity(quantity);
        } else {
            data.put(product.getId(), new CartItem(product, quantity, product.getPrice()));
        }
    }

    public CartItem getItem(int pid) {
        return data.get(pid);
    }

    public void addItemDirectly(CartItem item) {
        if (item != null) {
            CartItem newItem = new CartItem(item.getProduct(), item.getQuantity(), item.getPrice());
            data.put(newItem.getProduct().getId(), newItem);
        }
    }

    public void remove(int pid) {
        data.remove(pid);
    }

    public boolean deleteProduct(int pid){
        return data.remove(pid) != null;
    }

    public void deleteAll() {
        data.clear();
    }

    public List<CartItem> getList(){
        return new ArrayList<>(data.values());
    }

    public int getTotalQuantity() {
        return data.values().stream().mapToInt(CartItem::getQuantity).sum();
    }
    public double getTotal(){
        return data.values().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }
    public void updateQuantity(int pid, int quantity) {
        CartItem item = data.get(pid);
        if (item != null) {
            if (quantity < 1) {
                item.setQuantity(1);
            } else {
                item.setQuantity(quantity);
            }
        }
    }
}