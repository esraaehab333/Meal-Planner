package com.example.mealplanner.presentation.meal.view;

import com.example.mealplanner.data.models.Meal;

public interface MealView {
    void showMeal(Meal meal);
    void updateFavoriteStatus(boolean isFavorite);
    void showSuccessMessage(String message);
    void showErrorMessage(String message);
    void showLoading();
    void hideLoading();
}