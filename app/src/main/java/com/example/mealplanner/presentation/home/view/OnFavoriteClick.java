package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.models.Meal;

public interface OnFavoriteClick {
    void onClick(Meal meal);
    void onRemoveFromFavorite(Meal meal);
}