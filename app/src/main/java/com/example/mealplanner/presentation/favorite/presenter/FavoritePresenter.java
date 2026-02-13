package com.example.mealplanner.presentation.favorite.presenter;

import com.example.mealplanner.data.enitiy.FavoriteEntity;

public interface FavoritePresenter {
    void getFavoriteMeals();
    void deleteFavoriteMeal(FavoriteEntity favorite);
    void onDestroy();
}