package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.viewHolder.OrderDetailAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    ImageView btnBack;
    RecyclerView recyclerOrderDetail;
    OrderDetailAdapter adapter;
    private List<OrderDetail> orderDetailList;

    // Order DAO
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference orderDetails = database.getReference().child("orders").child("orderDetailList");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        btnBack = findViewById(R.id.btnBack);
        recyclerOrderDetail = findViewById(R.id.recycler_order_detail);

        recyclerOrderDetail.setHasFixedSize(true);
        recyclerOrderDetail.setLayoutManager(new LinearLayoutManager(this));

        orderDetailList = new ArrayList<>();
        adapter = new OrderDetailAdapter(orderDetailList);
        recyclerOrderDetail.setAdapter(adapter);

        // Load order detail list
        loadOrderDetails();

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(OrderDetailActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        };
        OrderDetailActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    // Load order detail list
    private void loadOrderDetails() {
        orderDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (OrderDetail od : orderDetailList) {
                            od = dataSnapshot.getValue(OrderDetail.class);
                            orderDetailList.add(od);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}