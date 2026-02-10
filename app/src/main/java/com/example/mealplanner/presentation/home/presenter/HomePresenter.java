package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.models.Meal;
import com.example.mealplanner.models.Category;

import java.util.List;

public interface HomePresenter {

    // API calls
    void getCategoryList();
    void getPopularList();
    void getMealOfDay();

    // ⭐ Favorite management
    void addToFavorite(Meal meal);
    void removeFromFavorite(Meal meal);
    List<String> getFavoriteMealIds();
}
