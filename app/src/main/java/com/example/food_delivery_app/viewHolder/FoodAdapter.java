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
import com.example.food_delivery_app.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FoodAdapter extends FirebaseRecyclerAdapter<Food, FoodAdapter.FoodViewHolder> {

    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
        holder.foodType.setText(model.getCatId());
        holder.foodName.setText(model.getName());
        Glide.with(holder.foodImage.getContext()).load(model.getImage()).into(holder.foodImage);

        holder.foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new FoodDetailFragment(model.getCatId(), model.getName(), model.getPrice(), model.getImage(), model.getQuantity()))
                        .addToBackStack(null).commit();
                activity.overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodType;
        ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodType = itemView.findViewById(R.id.foodType);
            foodImage = itemView.findViewById(R.id.food_image);

        }
    }
}
