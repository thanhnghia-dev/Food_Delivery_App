package com.example.food_delivery_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.common.Common;

public class SuccessFragment extends Fragment {
    TextView deliAddress, btnHome;
    Button btnOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deliAddress = view.findViewById(R.id.deliAddress);
        btnHome = view.findViewById(R.id.btnBackToHome);
        btnOrder = view.findViewById(R.id.btnMyOrder);

        if (Common.currentUser.getName() != null) {
            deliAddress.setText(Common.currentUser.getAddress());
        }

        // Button back home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HomeFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Button my order
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new OrderFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}