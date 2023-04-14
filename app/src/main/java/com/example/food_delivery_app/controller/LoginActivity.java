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
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.UserDAO;
import com.example.food_delivery_app.databinding.ActivityLoginBinding;
import com.example.food_delivery_app.fragment.AccountFragment;

public class LoginActivity extends AppCompatActivity {
    TextView btnSignup, btnForgotPass;
    Button btnLogin;
    ImageView btnBack;
    ActivityLoginBinding binding;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnSignup = findViewById(R.id.signUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.back);
        btnForgotPass = findViewById(R.id.forgotPass);

        userDAO = new UserDAO(this);

        // Button log in
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String phone = binding.edPhone.getText().toString();
                String password = binding.edPassword.getText().toString();

                if (phone.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Vui lòng không được để trống", Toast.LENGTH_LONG).show();
                } else {
                    password = userDAO.hashPassword(password);
                    boolean checkLogin = userDAO.checkLogin(phone, password);
                    if (checkLogin == true) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Số điện thoại hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Button forgot password
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });

        // Button sign up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        LoginActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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