package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.presenter.HomePresenter;
import com.example.mealplanner.presentation.home.presenter.HomePresenterImp;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView popularRecyclerView;

    private CategoryListAdapter categoryAdapter;
    private PopularListAdapter popularAdapter;

    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerViews();
        initPresenter();
        loadData();
    }

    private void initViews(View view) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        popularRecyclerView = view.findViewById(R.id.popularRecyclerView);
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryListAdapter();
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        categoriesRecyclerView.setAdapter(categoryAdapter);
        categoriesRecyclerView.setHasFixedSize(true);
        popularAdapter = new PopularListAdapter();
        popularRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        popularRecyclerView.setAdapter(popularAdapter);
        popularRecyclerView.setHasFixedSize(false);
    }
    private void initPresenter() {
        MealRemoteDataSource remoteDataSource = new MealRemoteDataSource();
        presenter = new HomePresenterImp(this, remoteDataSource);
    }
    private void loadData() {
        presenter.getCategoryList();
        presenter.getPopularList();
    }
    @Override
    public void onGetCategorySuccess(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            categoryAdapter.setCategoryList(categories);
        }
    }
    @Override
    public void onGetPopularSuccess(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            popularAdapter.setMealList(meals);
        } else {
            Toast.makeText(getContext(), "No popular meals found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoInternet() {
        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
    }
}