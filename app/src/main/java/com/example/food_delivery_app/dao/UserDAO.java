package com.example.food_delivery_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.security.MessageDigest;
import java.util.Base64;

public class UserDAO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "food_delivery.db";
    public static final String TABLE_NAME = "users";

    public UserDAO(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name VARCHAR(255) DEFAULT NULL, " +
                "phone VARCHAR(10) DEFAULT NULL, " +
                "gender TINYINT DEFAULT NULL, " +
                "address LONGTEXT DEFAULT NULL, " +
                "birthday DATE DEFAULT NULL, " +
                "password VARCHAR(255) DEFAULT NULL" + ")";
        myDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDatabase, int i, int i1) {
        myDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    // Register a new account
    public boolean register(String name, String phone, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("phone", phone);
        values.put("password", password);
        long result = myDB.insert("users", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Check is phone
    public boolean checkPhone(String phone) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM users WHERE phone = ?", new String[]{phone});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Check login
    public boolean checkLogin(String phone, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM users WHERE phone = ? AND password = ?", new String[]{phone, password});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Hash password
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String hashPassword(String pwd) {
        String salt = "wkfnsvksnclsvmslvn!*##15jhbusnb.";
        String result = null;

        pwd = pwd + salt;
        try {
            byte[] dataBytes = pwd.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            result = Base64.getEncoder().encodeToString(md.digest(dataBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
