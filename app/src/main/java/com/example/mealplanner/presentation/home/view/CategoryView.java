package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.data.models.Meal;

import java.util.List;

public interface CategoryView {
    void onGetMealsSuccess(List<Meal> meals);
    void onGetMealsError(String error);
}