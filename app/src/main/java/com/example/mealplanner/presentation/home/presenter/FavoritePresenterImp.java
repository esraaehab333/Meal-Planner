package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.models.FavoriteEntity;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.view.FavoriteView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImp implements FavoritePresenter {

    private FavoriteView view;
    private FavoriteLocalDataSource localDataSource;
    private CompositeDisposable compositeDisposable;

    public FavoritePresenterImp(FavoriteView view, FavoriteLocalDataSource localDataSource) {
        this.view = view;
        this.localDataSource = localDataSource;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getFavoriteMeals() {
        view.showLoading();
        compositeDisposable.add(
                localDataSource.getFavoriteMeals()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                favoriteEntities -> {
                                    view.hideLoading();
                                    if (favoriteEntities.isEmpty()) {
                                        view.showEmptyState();
                                    } else {
                                        view.hideEmptyState();
                                        List<Meal> meals = mapFavoritesToMeals(favoriteEntities);
                                        view.showFavoriteMeals(meals);
                                    }
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showErrorMessage("Failed to load list of favorite meals");
                                }
                        )
        );
    }

    @Override
    public void deleteFavoriteMeal(FavoriteEntity favorite) {
        view.showLoading();
        compositeDisposable.add(
                localDataSource.deleteFavoriteMeal(favorite)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.showSuccessMessage("Meal Deleted Successfully!");
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showErrorMessage("Failed to Delete meal");
                                }
                        )
        );
    }
    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private List<Meal> mapFavoritesToMeals(List<FavoriteEntity> entities) {
        List<Meal> meals = new ArrayList<>();
        for (FavoriteEntity entity : entities) {
            Meal meal = new Meal();
            meal.setIdMeal(entity.idMeal);
            meal.setStrMeal(entity.strMeal);
            meal.setStrMealThumb(entity.strMealThumb);
            meal.setStrCategory(entity.strCategory);
            meal.setStrArea(entity.strArea);
            meal.setStrTags(entity.strTags);
            meal.setStrYoutube(entity.strYoutube);
            meal.setStrInstructions(entity.strInstructions);
            meal.setIngredientsFromEntity(entity);
            meal.setMeasuresFromEntity(entity);
            meals.add(meal);
        }
        return meals;
    }
}