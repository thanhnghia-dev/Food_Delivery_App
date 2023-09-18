package com.example.food_delivery_app.viewHolder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.controller.OrderDetailActivity;
import com.example.food_delivery_app.model.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderAdapter extends FirebaseRecyclerAdapter<Order, OrderAdapter.OrderViewHolder> {

    public OrderAdapter(@NonNull FirebaseRecyclerOptions<Order> options) {
        super(options);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {
        holder.orderStatus.setText(Common.updateOrderStatus(model.getStatus()));
        holder.orderId.setText("#"+ handleShorterId(getRef(position).getKey()));
        holder.orderAddress.setText(model.getAddress());
        holder.orderPhone.setText(model.getPhone());
        holder.orderDate.setText(model.getOrderDate());
        holder.total.setText("Thành tiền: " + model.getTotal());

        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    // Shorter order id
    private String handleShorterId(String id) {
        return id.substring(4);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderStatus, orderId, orderAddress, orderPhone, orderDate, total;
        CardView orderView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderId = itemView.findViewById(R.id.orderId);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderPhone = itemView.findViewById(R.id.orderPhone);
            orderDate = itemView.findViewById(R.id.orderDate);
            total = itemView.findViewById(R.id.total);
            orderView = itemView.findViewById(R.id.orderView);
        }
    }
}
