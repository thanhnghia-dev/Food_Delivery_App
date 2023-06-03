package com.example.food_delivery_app.viewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAdapter extends FirebaseRecyclerAdapter<Category, MenuAdapter.MenuViewHolder> {

    public MenuAdapter(@NonNull FirebaseRecyclerOptions<Category> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
        holder.menuName.setText(model.getName());
        Glide.with(holder.imageView.getContext()).load(model.getImage()).into(holder.imageView);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        CircleImageView imageView, moveOn;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            menuName = itemView.findViewById(R.id.foodName);
            imageView = itemView.findViewById(R.id.foodImage);
            moveOn = imageView.findViewById(R.id.move_on);
        }
    }
}
