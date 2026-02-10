package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.models.FavoriteEntity;
import com.example.mealplanner.models.Meal;

public interface FavoritePresenter {
    void getFavoriteMeals();
    void deleteFavoriteMeal(FavoriteEntity favorite);
    void onDestroy();
}