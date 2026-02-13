package com.example.mealplanner.presentation.favorite.view;

import com.example.mealplanner.data.models.Meal;

import java.util.List;

public interface FavoriteView {
    void showFavoriteMeals(List<Meal> meals);
    void showSuccessMessage(String message);
    void showErrorMessage(String message);
    void showLoading();
    void hideLoading();
    void showEmptyState();
    void hideEmptyState();
}