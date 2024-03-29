package com.example.food_delivery_app.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;
import com.example.food_delivery_app.controller.OrderActivity;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.model.Order;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.viewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                handleConfirmOrder();
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

    // Handle confirm order
    private void handleConfirmOrder() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String createNow = date.format(formatter);

            Order order = new Order(System.currentTimeMillis(),
                    Common.currentUser.getName(),
                    Common.currentUser.getPhone(),
                    deliAddress.getText().toString(),
                    cart,
                    totalMoney.getText().toString(),
                    createNow);

            orders.child(String.valueOf(System.currentTimeMillis())).setValue(order);

            new Database(getContext()).clearCart();

            showSuccessDialog();
        }
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
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edHome = dialog.findViewById(R.id.edHome);
        EditText edCity = dialog.findViewById(R.id.edCity);
        EditText edProvince = dialog.findViewById(R.id.edProvince);
        Button btnYes = dialog.findViewById(R.id.btnSend);
        Button btnNo = dialog.findViewById(R.id.btnCancel);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String home = edHome.getText().toString();
                String city = edCity.getText().toString();
                String province = edProvince.getText().toString();

                if (home.isEmpty() || city.isEmpty() || province.isEmpty() ) {
                    Toast.makeText(getActivity(), "Vui lòng không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    deliAddress.setText(home + ", " + city + ", " + province);
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
        LocalDate date;
        String address = deliveryAddress();
        StringTokenizer tokenizer = new StringTokenizer(address, ",");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();

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
            if (day == month) {
                fee = 0;
            }
        }
        return fee;
    }

    // Calculate discount
    private int discount() {
        int discount = 0;
        int temp = 0;
        cart = new Database(getActivity()).getAll();
        LocalDate date;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now();
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();

            for (OrderDetail orderDetail : cart) {
                temp += Integer.parseInt(orderDetail.getPrice()) * orderDetail.getQuantity();
            }
            if (day == month) {
                discount = (int) (temp * 0.5);
            }
        }
        return discount;
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }

    // Show bottom success dialog
    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_success_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        Button btnOrder = dialog.findViewById(R.id.btnMyOrder);
        TextView btnHome = dialog.findViewById(R.id.btnBackToHome);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        // Button back home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                replaceFragment(new HomeFragment());
            }
        });

        // Button my order
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
            }
        });

        // Button close
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                replaceFragment(new HomeFragment());
            }
        });
        dialog.show();
    }
}