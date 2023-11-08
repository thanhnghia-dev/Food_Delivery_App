package com.example.food_delivery_app.fragment;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.dao.Database;
import com.example.food_delivery_app.model.OrderDetail;
import com.example.food_delivery_app.viewHolder.CartAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryFragment extends Fragment {
    ImageView btnBack;
    TextView subTotal, totalMoney, discount;
    Button btnPayment;
    RecyclerView recyclerCheckout;
    CartAdapter adapter;
    List<OrderDetail> cart = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        subTotal = view.findViewById(R.id.subTotal);
        totalMoney = view.findViewById(R.id.totalMoney);
        discount = view.findViewById(R.id.discount);
        btnPayment = view.findViewById(R.id.btnPayment);
        recyclerCheckout = view.findViewById(R.id.recycler_checkout);

        recyclerCheckout.setHasFixedSize(true);
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load list cart
        loadCartList();

        // Load transport fee
        discount.setText("-" + discount() + " \u20AB");

        // Button payment
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CheckoutFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CartFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                replaceFragment(new CartFragment());
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
        total += temp - discount();
        subTotal.setText(temp + " \u20AB");
        totalMoney.setText(total + " \u20AB");
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
}