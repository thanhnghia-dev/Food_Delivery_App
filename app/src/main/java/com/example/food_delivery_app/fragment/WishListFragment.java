package com.example.food_delivery_app.fragment;

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
import android.widget.ImageView;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.model.Order;
import com.example.food_delivery_app.model.WishList;
import com.example.food_delivery_app.viewHolder.OrderAdapter;
import com.example.food_delivery_app.viewHolder.WishListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class WishListFragment extends Fragment {
    ImageView btnBack;
    RecyclerView recyclerWishList;
    WishListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        recyclerWishList = view.findViewById(R.id.recycler_wish_list);

        recyclerWishList.setHasFixedSize(true);
        recyclerWishList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load wish list
        FirebaseRecyclerOptions<WishList> options = new FirebaseRecyclerOptions.Builder<WishList>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("wishLists"), WishList.class)
                .build();

        adapter = new WishListAdapter(options);
        recyclerWishList.setAdapter(adapter);

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HomeFragment());
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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}