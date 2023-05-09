package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText edtPhone, edtPass;
    TextView btnSignup, btnForgotPass;
    Button btnLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView btnGoogle, btnFacebook;

    // User DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference users = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edPhone);
        edtPass = findViewById(R.id.edPassword);
        btnSignup = findViewById(R.id.signUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPass = findViewById(R.id.forgotPass);
        btnGoogle = findViewById(R.id.login_google);
        btnFacebook = findViewById(R.id.login_facebook);

        // Google Sign in
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Button log-in
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                logIn();
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

        // Button sign-up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });

        // Button log-in with google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInWithGoogle();
            }
        });

        // Button log-in with facebook
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Login Facebook", Toast.LENGTH_SHORT).show();
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

    // Manual login
    private void logIn() {
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

    // Login with Google
    private void logInWithGoogle() {
        Intent logInIntent = gsc.getSignInIntent();
        startActivityForResult(logInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Switch to main activity
    private void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, AboutUsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}