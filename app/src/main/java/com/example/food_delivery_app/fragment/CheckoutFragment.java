package com.example.food_delivery_app.fragment;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.model.Order;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.viewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CheckoutFragment extends Fragment {
    ImageView btnBack;
    TextView deliAddress, btnChange, subTotal, transportFee, discount, totalMoney;
    RadioButton payCash, payEWallet, payATM;
    Button btnPayment;
    RecyclerView recyclerCheckout;
    CartAdapter adapter;
    List<OrderDetail> cart = new ArrayList<>();

    // Order DAO
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference orders = database.getReference("orders");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        deliAddress = view.findViewById(R.id.deliAddress);
        btnChange = view.findViewById(R.id.btnChange);
        payCash = view.findViewById(R.id.payCash);
        payEWallet = view.findViewById(R.id.payEWallet);
        payATM = view.findViewById(R.id.payATM);
        subTotal = view.findViewById(R.id.subTotal);
        transportFee = view.findViewById(R.id.transportFee);
        discount = view.findViewById(R.id.discount);
        totalMoney = view.findViewById(R.id.totalMoney);
        btnPayment = view.findViewById(R.id.btnPayment);
        recyclerCheckout = view.findViewById(R.id.recycler_checkout);

        recyclerCheckout.setHasFixedSize(true);
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load list cart
        loadCartList();

        // Load delivery address
        deliAddress.setText(deliveryAddress());

        // Load transport fee
        transportFee.setText(transportFee() + " \u20AB");

        // Load transport fee
        discount.setText("-" + discount() + " \u20AB");

        // Button change delivery address
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDeliAddress();
            }
        });

        // Button payment
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order(Common.currentUser.getName(),
                                        Common.currentUser.getPhone(),
                                        deliAddress.getText().toString(),
                                        cart,
                                        totalMoney.getText().toString());

                orders.child(String.valueOf(System.currentTimeMillis())).setValue(order);

                new Database(getContext()).clearCart();

                replaceFragment(new SuccessFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new DeliveryFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                replaceFragment(new DeliveryFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

    }
    
    // Load list cart
    @SuppressLint("SetTextI18n")
    private void loadCartList() {
        cart = new Database(getActivity()).getAll();
        adapter = new CartAdapter(getContext(), cart);
        recyclerCheckout.setAdapter(adapter);

        int temp = 0;
        int total = 0;
        for (OrderDetail orderDetail : cart) {
            temp += Integer.parseInt(orderDetail.getPrice()) * orderDetail.getQuantity();
        }
        total += temp + transportFee() - discount();
        subTotal.setText(temp + " \u20AB");
        totalMoney.setText(total + " \u20AB");
    }

    // Load delivery address
    private String deliveryAddress() {
        String address = "";
        if (Common.currentUser.getName() != null) {
            address += Common.currentUser.getAddress();
        }
        return address;
    }

    // Change delivery address
    private void changeDeliAddress() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_change_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edAddress = dialog.findViewById(R.id.edAddress);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    deliAddress.setText(edAddress.getText());
                    Toast.makeText(getActivity(), "Địa chỉ giao hàng được cập nhật!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
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

    // Calculate transport fee
    private int transportFee() {
        int fee = 0;
        String address = deliveryAddress();
        StringTokenizer tokenizer = new StringTokenizer(address, ",");

        while (tokenizer.hasMoreTokens()) {
            String ward = tokenizer.nextToken();
            String district = tokenizer.nextToken();
            String city = tokenizer.nextToken();

            if (city.equals(" TP. HCM")) {
                fee = 10000;
            } else if (city.equals(" Bình Dương")) {
                fee = 15000;
            } else if (city.equals(" Đồng Nai")) {
                fee = 20000;
            } else {
                fee = 25000;
            }
        }
        return fee;
    }

    // Calculate discount
    private int discount() {
        int fee = 0;

        return fee;
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}