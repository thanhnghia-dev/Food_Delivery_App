package com.example.food_delivery_app.model;

import java.io.Serializable;
import java.util.HashMap;

public class Cart implements Serializable {
    private int id;
    private HashMap<String, Food> productList;
    private User customer;
    private double totalMoney;
    private int quantity;

    public Cart() {
        productList = new HashMap<>();
        customer = new User();
        totalMoney = 0;
        quantity = 0;
    }

    public Cart(int id, User customer, double totalMoney, int quantity) {
        this.id = id;
        this.productList = new HashMap<>();
        this.customer = customer;
        this.totalMoney = totalMoney;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, Food> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<String, Food> productList) {
        this.productList = productList;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
