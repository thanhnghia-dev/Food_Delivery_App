package com.example.food_delivery_app.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String foodName;
    private String foodImage;
    private int quantity;
    private String price;

    public OrderDetail() {
    }

    public OrderDetail(String foodName, int quantity, String price) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderDetail(String foodName, String foodImage, int quantity, String price) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.quantity = quantity;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
