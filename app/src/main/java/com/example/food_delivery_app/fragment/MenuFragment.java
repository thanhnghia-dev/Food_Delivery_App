package com.example.food_delivery_app.fragment;

import android.content.Intent;
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
import com.example.food_delivery_app.controller.MainActivity;
import com.example.food_delivery_app.model.Category;
import com.example.food_delivery_app.viewHolder.MenuAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuFragment extends Fragment {
    ImageView btnBack;
    RecyclerView recyclerMenu;
    MenuAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        recyclerMenu = view.findViewById(R.id.recycler_menu);

        recyclerMenu.setHasFixedSize(true);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load category list
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("categories"), Category.class)
                        .build();
        adapter = new MenuAdapter(options);
        recyclerMenu.setAdapter(adapter);

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

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}