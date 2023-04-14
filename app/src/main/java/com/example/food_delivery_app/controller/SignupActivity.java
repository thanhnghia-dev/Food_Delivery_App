package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.UserDAO;
import com.example.food_delivery_app.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    Button btnSignup;
    ImageView btnBack;
    ActivitySignupBinding binding;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnSignup = findViewById(R.id.btnSignup);
        btnBack = findViewById(R.id.back);
        userDAO = new UserDAO(this);

        // Button sign up
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String name = binding.edName.getText().toString();
                String phone = binding.edPhone.getText().toString();
                String password = binding.edPassword.getText().toString();
                String confirmPassword = binding.edRePass.getText().toString();

                if (name.equals("") || phone.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignupActivity.this, "Vui lòng không được để trống", Toast.LENGTH_LONG).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        boolean checkUserPhone = userDAO.checkPhone(phone);
                        if (checkUserPhone == false) {
                            password = userDAO.hashPassword(password);
                            boolean insert = userDAO.register(name, phone, password);

                            if (insert == true) {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        SignupActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}