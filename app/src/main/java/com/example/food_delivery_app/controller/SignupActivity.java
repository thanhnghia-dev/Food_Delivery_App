package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.UserDAO;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    EditText edtName, edtPhone, edtPass, edtConfPass;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName = findViewById(R.id.edName);
        edtPhone = findViewById(R.id.edPhone);
        edtPass = findViewById(R.id.edPassword);
        edtConfPass = findViewById(R.id.edRePass);
        btnSignup = findViewById(R.id.btnSignup);

        // User DAO
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        // Button sign-up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                int id = 0;
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
                                if (snapshot.child(phone).exists()) {
                                    Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                                } else {
                                    int id = 1;
                                    id++;
                                    User user = new User(id, name, phone, null, null, null, password);
                                    users.child(phone).setValue(user);

                                    Toast.makeText(SignupActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
}