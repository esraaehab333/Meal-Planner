package com.example.mealplanner.presentation.planner.presenter;

import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.presentation.planner.view.PlannerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenterImp implements PlannerPresenter {

    private PlannerView view;
    private MealRepository mealRepository;
    private CompositeDisposable compositeDisposable;
    private String currentUserId;

    public PlannerPresenterImp (PlannerView view, MealRepository mealRepository, String userId) {
        this.view = view;
        this.mealRepository = mealRepository;
        this.currentUserId = userId;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadPlannedMealsForDate(String selectedDate) {
        view.showLoading();
        compositeDisposable.add(
                mealRepository.getMealsByDate(selectedDate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                planEntities -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showPlannedMeals(planEntities);
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showErrorMessage("Failed to load meals");
                                    }
                                }
                        )
        );
    }

    @Override
    public void addMealToPlan(Meal meal, String selectedDate) {
        PlanEntity planEntity = convertMealToPlanEntity(meal, selectedDate);
        view.showLoading();
        compositeDisposable.add(
                mealRepository.insertMealToPlan(planEntity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showSuccessMessage("Meal added to plan successfully");
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showErrorMessage("Failed to add meal");
                                    }
                                }
                        )
        );
    }

    @Override
    public void removeMealFromPlan(PlanEntity planEntity) {
        compositeDisposable.add(
                mealRepository.deleteMealFromPlan(planEntity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (view != null) {
                                        view.showSuccessMessage("Meal deleted from your plan");
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showErrorMessage("Error deleting meal: " + throwable.getMessage());
                                    }
                                }
                        )
        );
    }

    private PlanEntity convertMealToPlanEntity(Meal meal, String selectedDate) {
        List<String> ingredientsList = meal.getIngredientsList();
        List<String> measuresList = meal.getMeasuresList();
        String[] ingredients = new String[20];
        String[] measures = new String[20];
        for (int i = 0; i < 20; i++) {
            ingredients[i] = (ingredientsList != null && i < ingredientsList.size())
                    ? ingredientsList.get(i) : null;
            measures[i] = (measuresList != null && i < measuresList.size())
                    ? measuresList.get(i) : null;
        }
        return new PlanEntity(
                selectedDate,
                meal.getIdMeal(),
                currentUserId,
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
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}