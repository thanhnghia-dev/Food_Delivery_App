package com.example.food_delivery_app.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.viewHolder.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    TextView totalMoney;
    Button btnClearCart, btnCheckout;
    RecyclerView recyclerCart;
    CartAdapter adapter;
    List<OrderDetail> cart = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalMoney = view.findViewById(R.id.totalMoney);
        btnClearCart = view.findViewById(R.id.btnClear);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        recyclerCart = view.findViewById(R.id.recycler_cart);

        recyclerCart.setHasFixedSize(true);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load list cart
        loadCartList();

        // Button clear cart
        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart = new Database(getActivity()).getAll();

                for (OrderDetail orderDetail : cart) {
                    if (orderDetail == null) {
                        btnClearCart.setEnabled(false);
                    } else {
                        displayClearCartDialog();
                    }
                }
            }
        });

        // Button checkout
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart = new Database(getActivity()).getAll();

                for (OrderDetail orderDetail : cart) {
                    if (orderDetail == null) {
                        btnCheckout.setEnabled(false);
                    } else {
                        replaceFragment(new DeliveryFragment());
                        getActivity().overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    }
                }
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                replaceFragment(new HomeFragment());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

    }

    // Load list cart
    @SuppressLint("SetTextI18n")
    private void loadCartList() {
        cart = new Database(getActivity()).getAll();
        adapter = new CartAdapter(getContext(), cart);
        recyclerCart.setAdapter(adapter);

        int total = 0;
        for (OrderDetail orderDetail : cart) {
            total += Integer.parseInt(orderDetail.getPrice()) * orderDetail.getQuantity();
        }
        totalMoney.setText(total + " \u20AB");
    }

    // Display clear cart alert dialog
    private void displayClearCartDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_clear_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvMessage = dialog.findViewById(R.id.message);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        tvMessage.setText("Bạn có muốn xóa toàn bộ giỏ hàng?");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getContext()).clearCart();
                Toast.makeText(getActivity(), "Giỏ hàng đã xóa", Toast.LENGTH_SHORT).show();
                replaceFragment(new CartFragment());
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

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}