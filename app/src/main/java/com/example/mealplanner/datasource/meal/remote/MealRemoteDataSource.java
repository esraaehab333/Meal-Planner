package com.example.mealplanner.datasource.meal.remote;

import com.example.mealplanner.data.network.MealService;
import com.example.mealplanner.data.network.NetworkApi;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.models.CategoryResponse;
import com.example.mealplanner.data.models.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRemoteDataSource {

    private MealService mealService;

    public MealRemoteDataSource() {
        this.mealService = new NetworkApi().getMealService();
    }

    public Single<List<Category>> getCategoryList() {
        return mealService.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(CategoryResponse::getCategories);
    }

    public Single<List<Meal>> getPopularList() {
        return mealService.getPopularMeals("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MealResponse::getMeals);
    }

    public Single<List<Meal>> getMealOfDay() {
        return mealService.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MealResponse::getMeals);
    }
}
