package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText edtPhone, edtPass;
    TextView btnSignup, btnForgotPass;
    Button btnLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView btnGoogle, btnFacebook;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    private static final int RC_SIGN_IN = 9001;

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
        progressDialog = new ProgressDialog(this);

        // Google Sign in
        auth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        // Check Firebase user is activated
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            finish();
        }

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
                googleSignIn();
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

        progressDialog.setMessage("Chờ xíu...");
        progressDialog.show();
        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(phone).exists()) {
                        User user = snapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);

                        if (user.getPassword().equals(password)) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                            progressDialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản chưa được đăng ký!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // Login with Google
    private void googleSignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                cancelGoogleSignIn();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();

                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getUid());
                    map.put("name", user.getDisplayName());
                    map.put("phone", user.getPhoneNumber());
                    map.put("profile", user.getPhotoUrl().toString());

                    users.child(user.getUid()).setValue(map);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Confirm message when Google sign-in failed
    private void cancelGoogleSignIn() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Đăng Nhập");
        alertDialog.setMessage("Đăng nhập Google không thành công");

        alertDialog.setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
}