package com.example.food_delivery_app.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.OrderDetail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class OrderDetailAdapter extends FirebaseRecyclerAdapter<OrderDetail, OrderDetailAdapter.OrderDetailViewHolder> {

    public OrderDetailAdapter(@NonNull FirebaseRecyclerOptions<OrderDetail> options) {
        super(options);
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position, @NonNull OrderDetail model) {
        holder.foodName.setText(model.getFoodName());
        holder.quantity.setText(model.getQuantity());
        holder.price.setText(model.getPrice());
        Glide.with(holder.foodImage.getContext()).load(model.getFoodImage()).into(holder.foodImage);
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, quantity, price;
        ImageView foodImage;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodImage = itemView.findViewById(R.id.foodImage);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);

        }
    }
}
