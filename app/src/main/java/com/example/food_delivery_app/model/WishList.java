package com.example.food_delivery_app.model;

import java.io.Serializable;

public class WishList implements Serializable {
    private String name;
    private String image;
    private String price;
    private String customer;
    private String createAt;

    public WishList() {
    }

    public WishList(String name, String image, String price, String customer, String createAt) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.customer = customer;
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
