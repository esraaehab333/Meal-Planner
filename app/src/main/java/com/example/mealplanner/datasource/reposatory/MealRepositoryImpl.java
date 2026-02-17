package com.example.mealplanner.datasource.reposatory;

import android.app.Application;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.models.MealResponse;
import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.datasource.plan.local.PlanLocalDataSource;
import com.example.mealplanner.datasource.search.remote.SearchRemoteDataSource;
import com.example.mealplanner.presentation.favorite.presenter.FavoritePresenter;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {
    private FavoriteLocalDataSource favoriteLocalDataSource;
    private PlanLocalDataSource planLocalDataSource;
    private MealRemoteDataSource mealRemoteDataSource;
    private SearchRemoteDataSource searchRemoteDataSource;

    public MealRepositoryImpl(Application application, String userId){
        this.favoriteLocalDataSource = new FavoriteLocalDataSource(application,userId);
        this.mealRemoteDataSource=new MealRemoteDataSource();
        this.searchRemoteDataSource=new SearchRemoteDataSource();
        this.planLocalDataSource=new PlanLocalDataSource(application,userId);
    }
    @Override
    public Completable deleteAllFavorites() {
        return favoriteLocalDataSource.deleteAllFavorites();
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return favoriteLocalDataSource.isFavorite(mealId);
    }

    @Override
    public Completable deleteFavoriteMeal(FavoriteEntity favorite) {
        return favoriteLocalDataSource.deleteFavoriteMeal(favorite);
    }

    @Override
    public Completable insertFavoriteMeal(FavoriteEntity favorite) {
        return favoriteLocalDataSource.insertFavoriteMeal(favorite);
    }

    @Override
    public Flowable<List<FavoriteEntity>> getFavoriteMeals() {
        return favoriteLocalDataSource.getFavoriteMeals();
    }

    @Override
    public Single<MealResponse> getMealsByCategory(String categoryName) {
        return mealRemoteDataSource.getMealsByCategory(categoryName) ;
    }

    @Override
    public Single<List<Meal>> getMealOfDay() {
        return mealRemoteDataSource.getMealOfDay();
    }

    @Override
    public Single<List<Meal>> getPopularList() {
        return mealRemoteDataSource.getPopularList();
    }

    @Override
    public Completable deleteAllPlans() {
        return planLocalDataSource.deleteAllPlans();
    }

    @Override
    public Completable deleteMealFromPlan(PlanEntity plan) {
        return planLocalDataSource.deleteMealFromPlan(plan);
    }

    @Override
    public Completable insertMealToPlan(PlanEntity plan) {
        return planLocalDataSource.insertMealToPlan(plan);
    }

    @Override
    public Flowable<List<PlanEntity>> getMealsByDate(String selectedDate) {
        return planLocalDataSource.getMealsByDate(selectedDate);
    }

    @Override
    public Flowable<List<PlanEntity>> getAllPlannedMeals() {
        return planLocalDataSource.getAllPlannedMeals();
    }

    @Override
    public Single<List<Ingredient>> getIngredientsList() {
        return searchRemoteDataSource.getIngredientsList();
    }

    @Override
    public Single<List<Area>> getAreasList() {
        return searchRemoteDataSource.getAreasList();
    }

    @Override
    public Single<List<Category>> getCategoriesList() {
        return searchRemoteDataSource.getCategoriesList();
    }

    @Override
    public Single<List<Meal>> searchByName(String name) {
        return searchRemoteDataSource.searchByName(name);
    }

    @Override
    public Single<List<Meal>> getMealById(String mealId) {
        return mealRemoteDataSource.getMealById(mealId);
    }
}
