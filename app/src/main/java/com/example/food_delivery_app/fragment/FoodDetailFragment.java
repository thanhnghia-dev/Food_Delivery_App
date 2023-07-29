package com.example.food_delivery_app.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.model.Food;
import com.example.food_delivery_app.model.Order;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.model.WishList;
import com.example.food_delivery_app.viewHolder.OrderAdapter;
import com.example.food_delivery_app.viewHolder.WishListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodDetailFragment extends Fragment {
    ImageView foodImage, btnBack, btnWishList;
    ImageButton btnIncrease, btnDecrease;
    Button btnAddToCart;
    TextView foodId, foodName, foodPrice, quantity;
    String catId, name, image, price;
    int qty, totalQty;

    // Wish list DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference wishLists = database.getReference("wishLists");

    public FoodDetailFragment(String catId, String name, String price, String image) {
        this.catId = catId;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        btnBack = view.findViewById(R.id.btnBack);
        btnIncrease = view.findViewById(R.id.increase);
        btnDecrease = view.findViewById(R.id.decrease);
        btnWishList = view.findViewById(R.id.btnWishList);
        foodId = view.findViewById(R.id.title);
        foodImage = view.findViewById(R.id.foodImage);
        foodName = view.findViewById(R.id.foodName);
        foodPrice = view.findViewById(R.id.price);
        quantity = view.findViewById(R.id.quantity);
        btnBack = view.findViewById(R.id.btnBack);

        // Load food information
        loadDetailFood();

        // Button increase quantity
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(quantity.getText().toString());
                int inc = num + 1;

                quantity.setText(String.valueOf(inc));
            }
        });

        // Button decrease quantity
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(quantity.getText().toString());
                int dec = num - 1;

                if (dec <= 0) {
                    quantity.setEnabled(false);
                } else {
                    quantity.setText(String.valueOf(dec));
                }
            }
        });

        // Button add to cart
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodToCart();
            }
        });

        // Button add to wish list
        btnWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishList wishList = new WishList(foodName.getText().toString(),
                        image, foodPrice.getText().toString());
                wishLists.child(String.valueOf(System.currentTimeMillis())).setValue(wishList);

                btnWishList.setImageResource(R.drawable.icons_favorite_solid);
                Toast.makeText(getActivity(), "Sản phẩm đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HomeFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                replaceFragment(new HomeFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

    }

    // Load food detail
    private void loadDetailFood() {
        foodId.setText(catId);
        foodName.setText(name);
        foodPrice.setText(price + " \u20AB");
        Glide.with(getContext()).load(image).into(foodImage);
    }

    // Add food to cart
    private void addFoodToCart() {
        qty = Integer.parseInt(quantity.getText().toString());

        new Database(getContext()).addToCart(new OrderDetail(name, image, qty, price));
        Toast.makeText(getActivity(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        replaceFragment(new HomeFragment());
        getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}