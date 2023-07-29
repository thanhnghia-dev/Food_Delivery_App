package com.example.food_delivery_app.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String address;
    private List<OrderDetail> orderDetailList;
    private String total;
    private String status;

    public Order() {
    }

    public Order(String name, String phone, String address, List<OrderDetail> orderDetailList, String total) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.orderDetailList = orderDetailList;
        this.total = total;
        this.status = "0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
