package com.example.food_delivery_app.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.fragment.AccountFragment;
import com.example.food_delivery_app.fragment.OrderFragment;
import com.example.food_delivery_app.fragment.HomeFragment;
import com.example.food_delivery_app.fragment.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.bottom_nav);

        replaceFragment(new HomeFragment());
        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homes:
                        replaceFragment(new HomeFragment());
                        break;

                    case R.id.myOrder:
                        replaceFragment(new OrderFragment());
                        break;

                    case R.id.wishlist:
                        replaceFragment(new StoreFragment());
                        break;

                    case R.id.account:
                        replaceFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerAct, fragment);
        ft.commit();
    }
}