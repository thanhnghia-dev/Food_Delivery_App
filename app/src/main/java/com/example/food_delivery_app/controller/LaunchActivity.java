package com.example.food_delivery_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.databinding.ActivityLoginBinding;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Thread timing = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    Log.d("Test", "Error Start");
                } finally {
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timing.start();
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}