package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.fragment.HomeFragment;
import com.example.food_delivery_app.model.WishList;
import com.example.food_delivery_app.viewHolder.WishListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class WishListActivity extends AppCompatActivity {
    ImageView btnBack;
    RecyclerView recyclerWishList;
    WishListAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        btnBack = findViewById(R.id.btnBack);
        recyclerWishList = findViewById(R.id.recycler_wish_list);

        recyclerWishList.setHasFixedSize(true);
        recyclerWishList.setLayoutManager(new LinearLayoutManager(this));

        // Load wish list
        loadWishList(Common.currentUser.getName());

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WishListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(WishListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        WishListActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Load wish list
    private void loadWishList(String customer) {
        FirebaseRecyclerOptions<WishList> options = new FirebaseRecyclerOptions.Builder<WishList>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("wishLists").orderByChild("customer").equalTo(customer), WishList.class)
                .build();

        adapter = new WishListAdapter(options);
        recyclerWishList.setAdapter(adapter);
    }
}