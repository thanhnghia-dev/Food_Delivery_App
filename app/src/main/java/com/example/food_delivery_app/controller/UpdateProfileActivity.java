package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.fragment.AccountFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class UpdateProfileActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText tvFullName, tvPhone, tvEmail, tvAddress;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        tvFullName = findViewById(R.id.fullName);
        tvEmail = findViewById(R.id.email);
        tvPhone = findViewById(R.id.phone);
        tvAddress = findViewById(R.id.address);
        btnBack = findViewById(R.id.btnBack);

        // Load user information
        manualLogIn();
        GoogleLogIn();

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

    // Manual log-in
    private void manualLogIn() {
        if (Common.currentUser.getName() != null) {
            tvFullName.setText(Common.currentUser.getName());
            tvPhone.setText(Common.currentUser.getPhone());
            tvAddress.setText(Common.currentUser.getAddress());
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

            tvFullName.setText(perName);
            tvEmail.setText(email);
            tvPhone.setText("");
        }
    }
}