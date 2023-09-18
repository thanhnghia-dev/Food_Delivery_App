package com.example.food_delivery_app.viewHolder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.example.food_delivery_app.fragment.FoodDetailFragment;
import com.example.food_delivery_app.model.WishList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class WishListAdapter extends FirebaseRecyclerAdapter<WishList, WishListAdapter.WishListViewHolder> {

    public WishListAdapter(@NonNull FirebaseRecyclerOptions<WishList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WishListViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull WishList model) {
        holder.price.setText(model.getPrice());
        holder.foodName.setText(model.getName());
        Glide.with(holder.foodImage.getContext()).load(model.getImage()).into(holder.foodImage);

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(holder.foodImage.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_clear_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView tvMessage = dialog.findViewById(R.id.message);
                Button btnYes = dialog.findViewById(R.id.btnSend);
                Button btnNo = dialog.findViewById(R.id.btnCancel);

                tvMessage.setText("Bạn có muốn xóa khỏi yêu thích?");

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("wishLists")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.foodImage.getContext(), "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
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
        });
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item, parent, false);
        return new WishListViewHolder(view);
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, price;
        ImageView foodImage, trash;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            price = itemView.findViewById(R.id.price);
            foodImage = itemView.findViewById(R.id.foodImage);
            trash = itemView.findViewById(R.id.trash);

        }
    }
}
