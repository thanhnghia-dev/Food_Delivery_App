package com.example.food_delivery_app.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.fragment.FoodDetailFragment;
import com.example.food_delivery_app.model.WishList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class WishListAdapter extends FirebaseRecyclerAdapter<WishList, WishListAdapter.WishListViewHolder> {

    public WishListAdapter(@NonNull FirebaseRecyclerOptions<WishList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WishListViewHolder holder, int position, @NonNull WishList model) {
        holder.price.setText(model.getPrice());
        holder.foodName.setText(model.getName());
        Glide.with(holder.foodImage.getContext()).load(model.getImage()).into(holder.foodImage);
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item, parent, false);
        return new WishListViewHolder(view);
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, price;
        ImageView foodImage;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            price = itemView.findViewById(R.id.price);
            foodImage = itemView.findViewById(R.id.foodImage);

        }
    }
}
