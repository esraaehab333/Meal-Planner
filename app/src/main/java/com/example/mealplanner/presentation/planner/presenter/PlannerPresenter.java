package com.example.mealplanner.presentation.planner.presenter;

import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.enitiy.PlanEntity;

public interface PlannerPresenter {
    void loadPlannedMealsForDate(String selectedDate);
    void addMealToPlan(Meal meal, String selectedDate);
    void removeMealFromPlan(PlanEntity planEntity);
    void getPlannerMeal();
    void onDestroy();
}