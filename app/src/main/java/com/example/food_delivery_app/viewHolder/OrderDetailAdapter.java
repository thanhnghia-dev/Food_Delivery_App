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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetail> orderDetails;

    public OrderDetailAdapter(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail od = orderDetails.get(position);
        if (od == null) {
            return;
        }
        holder.foodName.setText(od.getFoodName());
        holder.quantity.setText(od.getQuantity());
        holder.price.setText(od.getPrice());
        Glide.with(holder.foodImage.getContext()).load(od.getFoodImage()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        if (orderDetails != null) {
            return orderDetails.size();
        }
        return 0;
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
