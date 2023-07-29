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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText edtName, edtPhone, edtPass, edtConfPass;
    TextView btnSignin;
    Button btnSignup;
    ProgressBar progressBar;

    final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*^]).{8,15})"; //1 digit from 0-9, 1 lowercase char, 1 uppercase char, 1 special symbol, length min = 8, max = 15
    final String PHONE_PATTERN = "(.{10,11})"; // length min = 6, max = 11

    // User DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference users = database.getReference("users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName = findViewById(R.id.edName);
        edtPhone = findViewById(R.id.edPhone);
        edtPass = findViewById(R.id.edPassword);
        edtConfPass = findViewById(R.id.edRePass);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignin = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressBar);

        // Button sign-up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                register();
            }
        });

        // Button sign-in
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });
        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        SignupActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    // Register a new account
    private void register() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPass.getText().toString();
        String confPassword = edtConfPass.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!password.equals(confPassword)) {
                Toast.makeText(SignupActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            } else {
                users.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (snapshot.child(phone).exists()) {
                            Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!isPhoneValid(phone)) {
                                Toast.makeText(SignupActivity.this, "Số điện thoại phải đủ 10 chữ số!", Toast.LENGTH_SHORT).show();
                            }
                            else if (!isPasswordValid(password)) {
                                Toast.makeText(SignupActivity.this, "Mật khẩu không đúng!\nMật khẩu nên bao gồm:\n1 chữ số, 1 ký tự viết hoa, 1 ký tự đặc biệt\nĐộ dài tối thiểu = 8 ký tự", Toast.LENGTH_LONG).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                User user = new User(name, phone, "", "", password);
                                users.child(phone).setValue(user);

                                Toast.makeText(SignupActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                    public boolean isPasswordValid(final String password) {
                        Pattern pattern;
                        Matcher matcher;
                        pattern = Pattern.compile(PASSWORD_PATTERN);
                        matcher = pattern.matcher(password);
                        return matcher.matches();
                    }

                    public boolean isPhoneValid(final String phone) {
                        Pattern pattern;
                        Matcher matcher;
                        pattern = Pattern.compile(PHONE_PATTERN);
                        matcher = pattern.matcher(phone);
                        return matcher.matches();
                    }
                });
            }
        }
    }
}