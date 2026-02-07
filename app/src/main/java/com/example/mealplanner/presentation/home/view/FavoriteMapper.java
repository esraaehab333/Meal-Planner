package com.example.mealplanner.presentation.home.view;

import com.example.mealplanner.models.FavoriteEntity;
import com.example.mealplanner.models.Meal;

public class FavoriteMapper {

    public static FavoriteEntity fromMeal(Meal meal, String userId) {
        return new FavoriteEntity(
                meal.getIdMeal(),
                userId,
                meal.getStrMeal(),
                meal.getStrMealThumb(),
                meal.getStrCategory(),
                meal.getStrArea(),
                meal.getStrTags(),
                meal.getStrYoutube(),
                meal.getStrInstructions(),
                meal.getIngredientsList().toArray(new String[0]),
                meal.getMeasuresList().toArray(new String[0])
        );
    }
}
