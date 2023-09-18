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
import android.widget.SearchView;
import android.widget.TextView;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.controller.MainActivity;
import com.example.food_delivery_app.model.Food;
import com.example.food_delivery_app.viewHolder.FoodAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FoodListFragment extends Fragment {
    ImageView btnBack;
    RecyclerView recyclerFood;
    FoodAdapter adapter;
    String name, image;
    TextView title;
    SearchView searchView;

    public FoodListFragment(String name, String image) {
        this.name = name;
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        title = view.findViewById(R.id.title);
        recyclerFood = view.findViewById(R.id.recycler_food);
        searchView = view.findViewById(R.id.menu_search);

        recyclerFood.setHasFixedSize(true);
        recyclerFood.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load food list
        loadFoodList(name);

        title.setText(name);

        // Search food
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                searchFood(keyword);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                searchFood(keyword);
                return false;
            }
        });

        // Button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new MenuFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        // Press back key
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                replaceFragment(new MenuFragment());
                getActivity().overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

    }

    // Load food list
    private void loadFoodList(String catId) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("foods").orderByChild("catId").equalTo(catId), Food.class)
                .build();

        adapter = new FoodAdapter(options);
        recyclerFood.setAdapter(adapter);
    }

    // Search food
    private void searchFood(String keyword) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("foods")
                        .orderByChild("name").startAt(keyword).endAt(keyword + "~"), Food.class)
                .build();

        adapter = new FoodAdapter(options);
        recyclerFood.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Replace Fragment for bottom nav
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}