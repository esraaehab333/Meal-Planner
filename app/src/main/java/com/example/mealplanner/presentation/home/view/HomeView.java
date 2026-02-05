package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;

import java.util.List;

public interface HomeView {
    void onGetCategorySuccess(List<Category> categories);
    void onGetPopularSuccess(List<Meal> meals);
    void onFailure(String errorMessage);
    void onNoInternet();
}