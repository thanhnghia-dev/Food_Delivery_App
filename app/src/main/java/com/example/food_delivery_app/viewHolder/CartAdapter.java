package com.example.food_delivery_app.viewHolder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.fragment.CartFragment;
import com.example.food_delivery_app.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<OrderDetail> listData = new ArrayList<>();

    public CartAdapter(Context context, List<OrderDetail> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.foodName.setText(listData.get(position).getFoodName());
        holder.quantity.setText(String.valueOf(listData.get(position).getQuantity()));
        holder.price.setText(listData.get(position).getPrice() + " \u20AB");
        Glide.with(holder.foodImage.getContext()).load(listData.get(position).getFoodImage()).into(holder.foodImage);

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayClearCartDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    // Clear cart item
    private void displayClearCartDialog(int position) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_clear_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvMessage = dialog.findViewById(R.id.message);
        Button btnYes = dialog.findViewById(R.id.btnSend);
        Button btnNo = dialog.findViewById(R.id.btnCancel);

        tvMessage.setText("Bạn có muốn xóa khỏi giỏ hàng?");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(context).clearCartItem(listData.get(position).getFoodName());
                Toast.makeText(context, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new CartFragment())
                        .addToBackStack(null).commit();
                dialog.dismiss();
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

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, quantity, price;
        ImageView foodImage, trash;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            trash = itemView.findViewById(R.id.trash);

        }
    }
}
