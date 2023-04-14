package com.example.food_delivery_app.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String gender;
    private String address;
    private String birthday;
    private String password;

    public User() {
    }

    public User(int id, String name, String phone, String gender, String address, String birthday, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.birthday = birthday;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
