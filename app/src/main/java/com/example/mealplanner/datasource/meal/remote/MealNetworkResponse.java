package com.example.mealplanner.datasource.meal.remote;

import com.example.mealplanner.models.Category;

import java.util.List;
// this is the callBack we use to update the ui
public interface MealNetworkResponse<T> {
    void onSuccess(List<T> List);
    void noInternet();
    void onFailure(String errorMessage);
}
