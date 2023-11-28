package com.example.food_delivery_app.controller;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.model.Order;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.viewHolder.OrderAdapter;
import com.example.food_delivery_app.viewHolder.OrderDetailAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView tvOrderId, tvOrderDate, tvOrderStatus, tvFullName, tvPhone, tvAddress, tvTotalMoney;
    RecyclerView recyclerOrderDetail;
    OrderDetailAdapter adapter;

    // Order DAO
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference orders = database.getReference("orders");
    DatabaseReference orderDetails = database.getReference("orders").child("orderDetailList");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvOrderId = findViewById(R.id.orderId);
        tvOrderDate = findViewById(R.id.orderDate);
        tvOrderStatus = findViewById(R.id.orderStatus);
        tvFullName = findViewById(R.id.fullName);
        tvPhone = findViewById(R.id.phone);
        tvAddress = findViewById(R.id.address);
        tvTotalMoney = findViewById(R.id.totalMoney);
        btnBack = findViewById(R.id.btnBack);
        recyclerOrderDetail = findViewById(R.id.recycler_order_detail);

        recyclerOrderDetail.setHasFixedSize(true);
        recyclerOrderDetail.setLayoutManager(new LinearLayoutManager(this));

        // Load order information
        loadOrderInfo(Common.currentUser.getPhone());
        // Load order-person information
        loadOrderPersonInfo(Common.currentUser.getPhone());
        // Load order detail list
        loadOrderDetails(Common.currentUser.getPhone());
        // Load total money
        loadTotalMoney(Common.currentUser.getPhone());

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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // Load order information
    private void loadOrderInfo(String phone) {
        orders.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Order order = dataSnapshot.getValue(Order.class);

                        if (order.getPhone().equals(phone)) {
                            tvOrderId.setText(handleShorterId(String.valueOf(order.getId())));
                            tvOrderDate.setText(order.getOrderDate());
                            tvOrderStatus.setText(Common.updateOrderStatus(order.getStatus()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Load order-person information
    private void loadOrderPersonInfo(String phone) {
        orders.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Order order = dataSnapshot.getValue(Order.class);

                        if (order.getPhone().equals(phone)) {
                            tvFullName.setText(order.getName());
                            tvPhone.setText(order.getPhone());
                            tvAddress.setText(order.getAddress());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Load total money
    private void loadTotalMoney(String phone) {
        orders.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Order order = dataSnapshot.getValue(Order.class);

                        if (order.getPhone().equals(phone)) {
                            tvTotalMoney.setText(order.getTotal());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Load order detail list
    private void loadOrderDetails(String phone) {
        FirebaseRecyclerOptions<OrderDetail> options = new FirebaseRecyclerOptions.Builder<OrderDetail>()
                .setQuery(orderDetails, OrderDetail.class)
                .build();

        adapter = new OrderDetailAdapter(options);
        recyclerOrderDetail.setAdapter(adapter);
    }

    private String handleShorterId(String id) {
        return id.substring(4);
    }

}