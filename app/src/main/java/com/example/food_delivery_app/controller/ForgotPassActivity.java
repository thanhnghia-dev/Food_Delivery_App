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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassActivity extends AppCompatActivity {
    EditText edtPhone;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        edtPhone = findViewById(R.id.edPhone);
        btnSubmit = findViewById(R.id.btnContinue);

        // User DAO
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        // Button submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();

                if (phone.isEmpty()) {
                    Toast.makeText(ForgotPassActivity.this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(phone).exists()) {
                                User user = snapshot.child(phone).getValue(User.class);
                                user.setPhone(phone);

                                Intent intent = new Intent(ForgotPassActivity.this, NewPassActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                            } else {
                                Toast.makeText(ForgotPassActivity.this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        ForgotPassActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}