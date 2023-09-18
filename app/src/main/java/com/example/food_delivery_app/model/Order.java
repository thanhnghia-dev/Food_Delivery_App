package com.example.food_delivery_app.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Order implements Serializable {
    private long id;
    private String name;
    private String phone;
    private String address;
    private List<OrderDetail> orderDetailList;
    private String total;
    private String orderDate;
    private String status;

    public Order() {
    }

    public Order(long id, String name, String phone, String address, List<OrderDetail> orderDetailList, String total, String orderDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.orderDetailList = orderDetailList;
        this.total = total;
        this.orderDate = orderDate;
        this.status = "0";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
