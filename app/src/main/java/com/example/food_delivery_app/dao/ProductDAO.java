package com.example.food_delivery_app.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.food_delivery_app.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "food_delivery.db";
    public static final String TABLE_NAME = "products";

    public ProductDAO(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id CHAR(7) PRIMARY KEY NOT NULL, " +
                "name VARCHAR(255) DEFAULT NULL, " +
                "image VARCHAR(255) DEFAULT NULL, " +
                "quantity TINYINT DEFAULT NULL, " +
                "price DECIMAL(10,1) DEFAULT NULL, " +
                "discount DECIMAL(10,1) DEFAULT NULL " + ")";
        myDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDatabase, int i, int i1) {
        myDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    // Get all product
    public List<Product> getAll() {
        List<Product> result = new ArrayList<>();
        SQLiteDatabase myDB = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = myDB.rawQuery(sql, new String[]{});
        cursor.moveToNext();

        Product product = new Product();
        while (cursor.isAfterLast() == false) {

        }
        return null;
    }
}
