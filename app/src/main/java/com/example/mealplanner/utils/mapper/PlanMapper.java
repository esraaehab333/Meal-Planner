package com.example.mealplanner.utils.mapper;

import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.enitiy.PlanEntity;

import java.util.List;

public class PlanMapper {
/*
    public static PlanEntity fromMeal(Meal meal, String userId,String date) {
        List<String> ingredientsList = meal.getIngredientsList();
        List<String> measuresList = meal.getMeasuresList();

        String[] ingredients = ingredientsList.toArray(new String[0]);
        String[] measures = measuresList.toArray(new String[0]);

        return new PlanEntity(
                date,
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
    }*/

    public static Meal toMeal(PlanEntity entity) {
        Meal meal = new Meal();
        meal.setIdMeal(entity.idMeal);
        meal.setStrMeal(entity.strMeal);
        meal.setStrMealThumb(entity.strMealThumb);
        meal.setStrCategory(entity.strCategory);
        meal.setStrArea(entity.strArea);
        meal.setStrTags(entity.strTags);
        meal.setStrYoutube(entity.strYoutube);
        meal.setStrInstructions(entity.strInstructions);
        meal.setIngredientsFromPlanEntity(entity);
        meal.setMeasuresFromPlanEntity(entity);

        return meal;
    }
}