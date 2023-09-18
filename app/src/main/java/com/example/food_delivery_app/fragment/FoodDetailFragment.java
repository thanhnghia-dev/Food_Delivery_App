package com.example.food_delivery_app.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.model.Rating;
import com.example.food_delivery_app.model.WishList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FoodDetailFragment extends Fragment {
    ImageView foodImage, btnBack, btnWishList, btnRating;
    ImageButton btnIncrease, btnDecrease;
    Button btnAddToCart;
    TextView foodId, foodName, foodPrice, quantity;
    String catId, name, image, price;
    int qty, totalQty;

    // Wish list DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference wishLists = database.getReference("wishLists");

    // Rating DAO
    final DatabaseReference ratings = database.getReference("ratings");

    public FoodDetailFragment(String catId, String name, String price, String image, int quantity) {
        this.catId = catId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.totalQty = quantity;
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
        btnRating = view.findViewById(R.id.rating);
        foodId = view.findViewById(R.id.title);
        foodImage = view.findViewById(R.id.foodImage);
        foodName = view.findViewById(R.id.foodName);
        foodPrice = view.findViewById(R.id.price);
        quantity = view.findViewById(R.id.quantity);
        btnBack = view.findViewById(R.id.btnBack);

        // Load food information
        loadDetailFood();

        // Button rating
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });

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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime date = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String createNow = date.format(formatter);

                    WishList wishList = new WishList(foodName.getText().toString(),
                            image, foodPrice.getText().toString(), Common.currentUser.getName(), createNow);
                    wishLists.child(String.valueOf(System.currentTimeMillis())).setValue(wishList);

                    btnWishList.setImageResource(R.drawable.icons_favorite_solid);
                    Toast.makeText(getActivity(), "Sản phẩm đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                }
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

        if (qty > totalQty) {
            Toast.makeText(getActivity(), "Số lượng mặt hàng không đủ", Toast.LENGTH_SHORT).show();
            quantity.setText(String.valueOf(totalQty));
            return;
        }
        new Database(getContext()).addToCart(new OrderDetail(name, image, qty, price));
        Toast.makeText(getActivity(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        replaceFragment(new HomeFragment());
        getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
    }

    // Show rating dialog
    private void showRatingDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_rating_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView message = dialog.findViewById(R.id.message);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        EditText edComment = dialog.findViewById(R.id.comment);
        Button btnSend = dialog.findViewById(R.id.btnSend);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        message.setText("Hãy cho chúng tôi biết đánh giá của bạn về sản phẩm này!");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = edComment.getText().toString();
                String reviewer = Common.currentUser.getName();
                float rateCount = ratingBar.getRating();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    if (rateCount == 0) {
                        Toast.makeText(getActivity(), "Vui lòng chọn đánh giá!", Toast.LENGTH_SHORT).show();
                    } else if (comment.isEmpty()) {
                        Toast.makeText(getActivity(), "Vui lòng nhập nội dung đánh giá!", Toast.LENGTH_SHORT).show();
                    } else {
                        LocalDateTime date = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String createNow = date.format(formatter);

                        Rating rating = new Rating(name, image, rateCount, comment, reviewer, createNow);
                        ratings.child(name).setValue(rating);

                        btnRating.setImageResource(R.drawable.baseline_star_24);
                        Toast.makeText(getActivity(), "Đánh giá đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }

}