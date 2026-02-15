package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.data.models.Meal;

import java.util.List;

public interface HomePresenter {
    void getCategoryList();
    void getPopularList();
    void getMealOfDay();
    void addToFavorite(Meal meal);
    void removeFromFavorite(Meal meal);
    List<String> getFavoriteMealIds();
}
