package com.example.mealplanner.presentation.home.view;
import com.example.mealplanner.models.PlanEntity;
import java.util.List;

public interface PlannerView {
    void showPlannedMeals(List<PlanEntity> meals);
    void showSuccessMessage(String message);
    void showErrorMessage(String message);
    void showLoading();
    void hideLoading();
}