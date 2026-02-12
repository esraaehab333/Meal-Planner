package com.example.mealplanner.presentation.search.presenter;

import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.models.Meal;
import java.util.List;

public interface SearchView {
    void onSearchSuccess(List<Meal> meals);
    void onSearchByNameSuccess(List<Meal> meals);
    void onSearchFailure(String errorMessage);
    void onNoInternet();
    void onDisplayListArea(List<Area> areas);
    void onDisplayListIngredients(List<Ingredient> ingredients);
    void onDisplayListCategory(List<Category> categories);
    void onLoading(boolean isLoading);

    // الميثود المسؤولة عن تمرير الوجبة الكاملة بعد الـ Lookup
    void onGetFullMealSuccess(Meal meal);
}