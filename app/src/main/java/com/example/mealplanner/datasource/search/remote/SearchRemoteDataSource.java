package com.example.mealplanner.datasource.search.remote;

import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.network.MealService;
import com.example.mealplanner.data.network.NetworkApi;
import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.IngredientMealDetails;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.models.MealResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchRemoteDataSource {
    private MealService mealService;

    public SearchRemoteDataSource() {
        this.mealService = new NetworkApi().getMealService();
    }
    public Single<List<Meal>> searchByName(String name) {
        return mealService.getMealByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MealResponse::getMeals);
    }

    public Single<List<Category>> getCategoriesList() {
        return mealService.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (response.getCategories() != null) {
                        return response.getCategories();
                    } else {
                        return new ArrayList<Category>();
                    }
                });
    }

    public Single<List<Area>> getAreasList() {
        return mealService.getAreasList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (response.getAreas() != null) {
                        return response.getAreas();
                    } else {
                        return new ArrayList<Area>();
                    }
                });
    }

    public Single<List<Ingredient>> getIngredientsList() {
        return mealService.getIngredientsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (response.getIngradiants() != null) {
                        return response.getIngradiants();
                    } else {
                        return new ArrayList<Ingredient>();
                    }
                });
    }
}