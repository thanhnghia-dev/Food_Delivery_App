package com.example.food_delivery_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.food_delivery_app.R;

public class OrderFragment extends Fragment {
    ImageView historyOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        historyOrder = view.findViewById(R.id.history_order);

        historyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace(new OrderHistoryFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
        return view;
    }

    // Replace an other fragment
    public void replace(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}