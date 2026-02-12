package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Meal;

import java.util.List;

public interface HomeView {
    void onGetCategorySuccess(List<Category> categories);
    void onGetPopularSuccess(List<Meal> meals);
    void onGetMealOfDaySuccess(List<Meal> meals);
    void onFailure(String errorMessage);
    void onNoInternet();
    void updateFavoriteIds(List<String> favoriteIds);
}