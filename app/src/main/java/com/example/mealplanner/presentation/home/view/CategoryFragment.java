package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.presentation.home.presenter.CategoryPresenter;
import com.example.mealplanner.presentation.home.presenter.CategoryPresenterImp;
import com.example.mealplanner.utils.CustomSnackbar;

import java.util.List;

public class CategoryFragment extends Fragment implements CategoryView, OnMealClick {

    private RecyclerView recyclerView;
    private CategoryMealsAdapter adapter;
    private CategoryPresenter presenter;
    private TextView tvTitle;
    private String categoryName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recipesRecyclerView);
        tvTitle = view.findViewById(R.id.toolbar_title);
        view.findViewById(R.id.btn_back).setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        if (getArguments() != null) {
            Category category = CategoryFragmentArgs.fromBundle(getArguments()).getCategory();
            categoryName = category.getStrCategory();
            tvTitle.setText(categoryName);
        }
        setupRecyclerView();
        presenter = new CategoryPresenterImp(this);
        presenter.getMealsByCategory(categoryName);

        return view;
    }

    private void setupRecyclerView() {
        adapter = new CategoryMealsAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetMealsSuccess(List<Meal> meals) {
        if (meals != null) {
            adapter.setMealList(meals);
        }
    }

    @Override
    public void onGetMealsError(String error) {
        if (getView() != null) {
            CustomSnackbar.showError(getView(), error);
        }
    }

    @Override
    public void onMealClick(Meal meal) {

    }

    @Override
    public void onAddToFavorite(Meal meal) { }

    @Override
    public void onRemoveFromFavorite(Meal meal) { }
}