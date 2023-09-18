package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.food_delivery_app.model.Order;
import com.example.food_delivery_app.viewHolder.OrderAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {
    ImageView btnBack;
    RecyclerView recyclerOrder;
    OrderAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btnBack = findViewById(R.id.btnBack);
        recyclerOrder = findViewById(R.id.recycler_order);

        recyclerOrder.setHasFixedSize(true);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));

        // Load order list
        loadOrders(Common.currentUser.getPhone());

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        OrderActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    // Load order list
    private void loadOrders(String phone) {
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("orders").orderByChild("phone").equalTo(phone), Order.class)
                .build();

        adapter = new OrderAdapter(options);
        recyclerOrder.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}