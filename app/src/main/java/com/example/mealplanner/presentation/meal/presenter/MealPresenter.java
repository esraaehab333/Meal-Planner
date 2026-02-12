package com.example.mealplanner.presentation.meal.presenter;

import com.example.mealplanner.data.models.Meal;

public interface MealPresenter {
    void loadMeal(Meal meal);
    void addToFav(Meal meal);
    void deleteFromFav(Meal meal);
    void isFavorite(String mealId);
    void onDestroy();
}