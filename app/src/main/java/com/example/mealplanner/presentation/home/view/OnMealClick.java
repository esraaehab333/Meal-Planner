package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.data.models.Meal;

public interface OnMealClick {
    void onMealClick(Meal meal);
    void onAddToFavorite(Meal meal);
    void onRemoveFromFavorite(Meal meal);
}
