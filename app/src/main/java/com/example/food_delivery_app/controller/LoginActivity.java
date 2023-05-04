package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.UserDAO;
import com.example.food_delivery_app.databinding.ActivityLoginBinding;
import com.example.food_delivery_app.fragment.AccountFragment;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText edtPhone, edtPass;
    TextView btnSignup, btnForgotPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edPhone);
        edtPass = findViewById(R.id.edPassword);
        btnSignup = findViewById(R.id.signUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPass = findViewById(R.id.forgotPass);

        // User DAO
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        // Button log-in
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();
                String password = edtPass.getText().toString();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(phone).exists()) {
                                User user = snapshot.child(phone).getValue(User.class);
                                user.setPhone(phone);

                                if (user.getPassword().equals(password)) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    Common.currentUser = user;
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);                        ;
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Tài khoản chưa được đăng ký!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        // Button forgot password
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });

        // Button sign up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               finish();
            }
        };
        LoginActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}