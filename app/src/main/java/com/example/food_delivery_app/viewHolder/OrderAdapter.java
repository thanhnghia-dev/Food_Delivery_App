package com.example.food_delivery_app.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.fragment.OrderDetailFragment;
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
        holder.orderId.setText(getRef(position).getKey());
        holder.orderAddress.setText(model.getAddress());
        holder.orderPhone.setText(model.getPhone());
        holder.total.setText(model.getTotal());

        holder.orderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new OrderDetailFragment())
                        .addToBackStack(null).commit();
                activity.overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderStatus, orderId, orderAddress, orderPhone, total;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderId = itemView.findViewById(R.id.orderId);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderPhone = itemView.findViewById(R.id.orderPhone);
            total = itemView.findViewById(R.id.total);

        }
    }
}
