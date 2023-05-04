package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewPassActivity extends AppCompatActivity {
    EditText edtPhone, edtConfPass, edtPass;
    Button btnSave;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);

        edtPhone = findViewById(R.id.edPhone);
        edtPass = findViewById(R.id.edPassword);
        edtConfPass = findViewById(R.id.edRePass);
        btnSave = findViewById(R.id.btnSave);

        // User DAO
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        // Button save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();
                String password = edtPass.getText().toString();
                String confPassword = edtConfPass.getText().toString();

                if (password.isEmpty() || confPassword.isEmpty()) {
                    Toast.makeText(NewPassActivity.this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!password.equals(confPassword)) {
                        Toast.makeText(NewPassActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    } else {
                        users.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = new User(0, null, null, null, null, null, password);
                                users.child(phone).setValue(user);

                                Toast.makeText(NewPassActivity.this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewPassActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
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
}