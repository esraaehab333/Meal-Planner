package com.example.mealplanner.datasource.reposatory;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.models.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    public Completable deleteAllFavorites() ;
    public Single<Boolean> isFavorite(String mealId) ;
    public Completable deleteFavoriteMeal(FavoriteEntity favorite);
    public Completable insertFavoriteMeal(FavoriteEntity favorite);
    public Flowable<List<FavoriteEntity>> getFavoriteMeals();
    public Single<MealResponse> getMealsByCategory(String categoryName);
    public Single<List<Meal>> getMealOfDay();
    public Single<List<Meal>> getPopularList();
    public Completable deleteAllPlans();
    public Completable deleteMealFromPlan(PlanEntity plan);
    public Completable insertMealToPlan(PlanEntity plan);
    public Flowable<List<PlanEntity>> getMealsByDate(String selectedDate);
    public Flowable<List<PlanEntity>> getAllPlannedMeals();
    public Single<List<Ingredient>> getIngredientsList();
    public Single<List<Area>> getAreasList();
    public Single<List<Category>> getCategoriesList();
    public Single<List<Meal>> searchByName(String name);
    public Single<List<Meal>> getMealById(String mealId);

}
