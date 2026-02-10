package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.models.Meal;
import com.example.mealplanner.models.PlanEntity;

public interface PlannerPresenter {
    void loadPlannedMealsForDate(String selectedDate);
    void addMealToPlan(Meal meal, String selectedDate);
    void removeMealFromPlan(PlanEntity planEntity);
    void onDestroy();
}