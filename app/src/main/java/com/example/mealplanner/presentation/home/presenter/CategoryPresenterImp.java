package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.presentation.home.view.CategoryView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryPresenterImp implements CategoryPresenter {
    private CategoryView view;
    private MealRemoteDataSource repo;

    public CategoryPresenterImp(CategoryView view) {
        this.view = view;
        this.repo = new MealRemoteDataSource();
    }

    public void getMealsByCategory(String categoryName) {
        repo.getMealsByCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response != null && response.getMeals() != null) {
                                view.onGetMealsSuccess(response.getMeals());
                            } else {
                                view.onGetMealsError("No meals found for this category");
                            }
                        },
                        throwable -> view.onGetMealsError(throwable.getMessage())
                );
    }
}
