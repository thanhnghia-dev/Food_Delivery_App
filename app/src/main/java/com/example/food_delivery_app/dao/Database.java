package com.example.food_delivery_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.food_delivery_app.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodDelivery.db";
    private static final String TABLE_NAME = "carts";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase myDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "foodName VARCHAR(255) DEFAULT NULL, " +
                "foodImage LONGTEXT DEFAULT NULL, " +
                "quantity TINYINT DEFAULT NULL, " +
                "price VARCHAR(255) DEFAULT NULL " + ")";
        myDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDatabase, int i, int i1) {
        myDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    // Get all order detail
    public List<OrderDetail> getAll() {
        List<OrderDetail> result = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String foodName = cursor.getString(1);
            String foodImage = cursor.getString(2);
            int quantity = cursor.getInt(3);
            String price = cursor.getString(4);

            result.add(new OrderDetail(foodName, foodImage, quantity, price));
            cursor.moveToNext();
        }
        return result;
    }

    // Add to cart
    public void addToCart(OrderDetail od) {
        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("foodName", od.getFoodName());
        values.put("foodImage", od.getFoodImage());
        values.put("quantity", od.getQuantity());
        values.put("price", od.getPrice());

        myDB.insert(TABLE_NAME, null, values);
        myDB.close();
    }

    // Clear cart
    public void clearCart() {
        SQLiteDatabase myDB = this.getWritableDatabase();
        myDB.delete(TABLE_NAME, null, null);
        myDB.close();
    }

    // Clear cart
    public void clearCartItem(String foodName) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        myDB.delete(TABLE_NAME, "foodName" + " = ?", new String[] { String.valueOf(foodName) });
        myDB.close();
    }
}
