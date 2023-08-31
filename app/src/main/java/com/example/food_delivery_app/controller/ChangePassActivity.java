package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText edtOldPass, edtConfPass, edtPass;
    Button btnSave;
    ProgressDialog progressDialog;
    final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*^]).{8,15})"; //1 digit from 0-9, 1 lowercase char, 1 uppercase char, 1 special symbol, length min = 8, max = 15

    // User DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference users = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        edtOldPass = findViewById(R.id.edOldPassword);
        edtPass = findViewById(R.id.edPassword);
        edtConfPass = findViewById(R.id.edRePass);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        progressDialog = new ProgressDialog(this);

        // Button save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChangePassword();
            }
        });

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        getOnBackPressedDispatcher().addCallback(ChangePassActivity.this, callback);

    }

    // Change old password
    private void handleChangePassword() {
        String oldPassword = edtOldPass.getText().toString();
        String password = edtPass.getText().toString();
        String confPassword = edtConfPass.getText().toString();
        String phone = Common.currentUser.getPhone();

        progressDialog.setMessage("Chờ xíu...");
        progressDialog.show();
        if (oldPassword.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            users.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(phone).exists()) {
                        if (!oldPassword.equals(Common.currentUser.getPassword())) {
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else if (!password.equals(confPassword)) {
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else if (password.equals(oldPassword) && confPassword.equals(oldPassword)) {
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu mới không được trùng với mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else if (!isPasswordValid(password)) {
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu không đúng!\nMật khẩu nên bao gồm:\n1 chữ số, 1 ký tự viết hoa, 1 ký tự đặc biệt\nĐộ dài tối thiểu = 8 ký tự", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                        else {
                            User user = new User(Common.currentUser.getName(), phone, Common.currentUser.getEmail(), Common.currentUser.getAddress(), password);
                            users.child(phone).setValue(user);

                            Toast.makeText(ChangePassActivity.this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(ChangePassActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
            });
        }
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}