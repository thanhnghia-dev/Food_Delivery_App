package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.fragment.AccountFragment;
import com.example.food_delivery_app.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText etFullName, etPhone, etEmail, etAddress;
    Button btnSave;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ProgressDialog progressDialog;

    // User DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference users = database.getReference("users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etFullName = findViewById(R.id.fullName);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etAddress = findViewById(R.id.address);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        progressDialog = new ProgressDialog(this);

        // Load user information
        manualLogIn();
        GoogleLogIn();

        // Button save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        getOnBackPressedDispatcher().addCallback(UpdateProfileActivity.this, callback);

    }

    // Update user information
    private void updateUserInfo() {
        String name = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();

        progressDialog.setMessage("Chờ xíu...");
        progressDialog.show();
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else {
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(phone).exists()) {
                        User user = new User(name, phone, email, address, Common.currentUser.getPassword());
                        users.child(phone).setValue(user);

                        Toast.makeText(UpdateProfileActivity.this, "Thông tin lưu thành công!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // Manual log-in
    private void manualLogIn() {
        if (Common.currentUser.getName() != null) {
            etFullName.setText(Common.currentUser.getName());
            etEmail.setText(Common.currentUser.getEmail());
            etPhone.setText(Common.currentUser.getPhone());
            etAddress.setText(Common.currentUser.getAddress());
        }
    }

    // Google log-in
    private void GoogleLogIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(UpdateProfileActivity.this, gso);

        // Set name and email
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(UpdateProfileActivity.this);
        if (account != null) {
            String perName = account.getDisplayName();
            String email = account.getEmail();

            etFullName.setText(perName);
            etEmail.setText(email);
            etPhone.setText("");
        }
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}