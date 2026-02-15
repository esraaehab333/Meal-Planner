package com.example.mealplanner.utils.mapper;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.models.Meal;

import java.util.List;

public class FavoriteMapper {

    public static FavoriteEntity fromMeal(Meal meal, String userId) {
        List<String> ingredientsList = meal.getIngredientsList();
        List<String> measuresList = meal.getMeasuresList();

        String[] ingredients = ingredientsList.toArray(new String[0]);
        String[] measures = measuresList.toArray(new String[0]);

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
                ingredients,
                measures
        );
    }
/*
    public static Meal toMeal(FavoriteEntity entity) {
        Meal meal = new Meal();
        meal.setIdMeal(entity.idMeal);
        meal.setStrMeal(entity.strMeal);
        meal.setStrMealThumb(entity.strMealThumb);
        meal.setStrCategory(entity.strCategory);
        meal.setStrArea(entity.strArea);
        meal.setStrTags(entity.strTags);
        meal.setStrYoutube(entity.strYoutube);
        meal.setStrInstructions(entity.strInstructions);
        meal.setIngredientsFromEntity(entity);
        meal.setMeasuresFromEntity(entity);

        return meal;
    }*/
}