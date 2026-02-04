package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.meal.remote.MealNetworkResponse;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.models.Category;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private CategoryListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        adapter = new CategoryListAdapter();
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        categoriesRecyclerView.setAdapter(adapter);
        loadCategories();
        return view;
    }

    private void loadCategories() {
        MealRemoteDataSource remoteDataSource = new MealRemoteDataSource();
        remoteDataSource.getCategoryList(new MealNetworkResponse() {
            @Override
            public void onSuccess(List<Category> categories) {
                adapter.setCategoryList(categories);
            }
            @Override
            public void onFailure(String errorMsg) {}
            @Override
            public void noInternet() {}
        });
    }

}
