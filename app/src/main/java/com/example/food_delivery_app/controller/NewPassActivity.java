package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewPassActivity extends AppCompatActivity {
    EditText edtConfPass, edtPass;
    Button btnSave;
    ProgressDialog progressDialog;
    final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*^]).{8,15})"; //1 digit from 0-9, 1 lowercase char, 1 uppercase char, 1 special symbol, length min = 8, max = 15

    // User DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference users = database.getReference("users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);

        edtPass = findViewById(R.id.edPassword);
        edtConfPass = findViewById(R.id.edRePass);
        btnSave = findViewById(R.id.btnSave);
        progressDialog = new ProgressDialog(this);

        // Button save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changePassword();
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(NewPassActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        NewPassActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    // Change a new password
    public void changePassword() {
        String password = edtPass.getText().toString();
        String confPassword = edtConfPass.getText().toString();

        progressDialog.setMessage("Chờ xíu...");
        progressDialog.show();
        if (password.isEmpty() || confPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
        } else {
            users.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!password.equals(confPassword)) {
                        Toast.makeText(NewPassActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else if (!isPasswordValid(password)) {
                        Toast.makeText(NewPassActivity.this, "Mật khẩu không đúng!\nMật khẩu nên bao gồm:\n1 chữ số, 1 ký tự viết hoa, 1 ký tự đặc biệt\nĐộ dài tối thiểu = 8 ký tự", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {
//                        User user = new User(Common.currentUser.getName(), Common.currentUser.getPhone(), Common.currentUser.getEmail(), Common.currentUser.getAddress(), password);
//                        users.child(phone).setValue(user);

                        Toast.makeText(NewPassActivity.this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewPassActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
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
}