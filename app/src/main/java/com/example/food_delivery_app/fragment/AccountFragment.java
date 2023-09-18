package com.example.food_delivery_app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.controller.ChangePassActivity;
import com.example.food_delivery_app.controller.LoginActivity;
import com.example.food_delivery_app.controller.UpdateProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    TextView tvFullName, tvWelcomeName, tvPhone, tvEmail, tvAddress;
    CircleImageView profileImage;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFullName = view.findViewById(R.id.fullName);
        tvWelcomeName = view.findViewById(R.id.my_name);
        tvEmail = view.findViewById(R.id.email);
        tvPhone = view.findViewById(R.id.phone);
        tvAddress = view.findViewById(R.id.address);
        profileImage = view.findViewById(R.id.profileImage);

        // Check current user and load profile
        if (Common.currentUser.getName() == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_out_right, R.anim.anim_out_left);
        } else {
            manualLogIn();
            GoogleLogIn();
        }

        // Choose and upload profile image
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "This is my avatar", Toast.LENGTH_SHORT).show();
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                replaceFragment(new HomeFragment());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.change_password) {
            Intent intent = new Intent(getActivity(), ChangePassActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
        }
        else if (id == R.id.update_profile) {
            Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
        }
        else if (id == R.id.log_out) {
            handleLogoutDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    // Manual log-in
    private void manualLogIn() {
        if (Common.currentUser.getName() != null) {
            tvWelcomeName.setText(Common.currentUser.getName());
            tvFullName.setText(Common.currentUser.getName());
            tvPhone.setText(Common.currentUser.getPhone());
            tvEmail.setText(Common.currentUser.getEmail());
            tvAddress.setText(Common.currentUser.getAddress());
        }
    }

    // Google log-in
    private void GoogleLogIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);

        // Set name and email
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            String perName = account.getDisplayName();
            String email = account.getEmail();

            tvWelcomeName.setText(perName);
            tvFullName.setText(perName);
            tvEmail.setText(email);
            tvPhone.setText("Số điện thoại");
            tvAddress.setText("Địa chỉ");
        }
    }

    // Handle display logout dialog
    private void handleLogoutDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvMessage = dialog.findViewById(R.id.message);
        Button btnYes = dialog.findViewById(R.id.btnSend);
        Button btnNo = dialog.findViewById(R.id.btnCancel);

        tvMessage.setText("Bạn có chắc chắn muốn đăng xuất?");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                if (account != null) {
                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent login = new Intent(getActivity(), LoginActivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(login);
                            getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                        }
                    });
                }
                else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(login);
                    getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}