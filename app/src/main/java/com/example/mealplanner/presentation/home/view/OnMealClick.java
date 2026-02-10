package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.models.Meal;
import com.example.mealplanner.models.Meal;

public interface OnMealClick {
    void onMealClick(Meal meal);
    void onAddToFavorite(Meal meal);
    void onRemoveFromFavorite(Meal meal);
}
