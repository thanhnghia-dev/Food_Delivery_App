package com.example.food_delivery_app.common;

import com.example.food_delivery_app.model.User;

public class Common {
    public static User currentUser;

    public static String updateOrderStatus(String status) {
        if (status.equals("0")) {
            return "Đang xử lý";
        } else if (status.equals("1")) {
            return "Đang vận chuyển";
        } else if (status.equals("2")) {
            return "Giao hàng thành công";
        } else {
            return "Đã hủy";
        }
    }
}
