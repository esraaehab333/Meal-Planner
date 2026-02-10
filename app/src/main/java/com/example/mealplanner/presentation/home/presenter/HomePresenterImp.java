package com.example.mealplanner.presentation.home.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Shader;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.datasource.meal.remote.MealNetworkResponse;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.models.FavoriteEntity;
import com.example.mealplanner.presentation.home.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImp implements HomePresenter {

    private HomeView view;
    private MealRemoteDataSource remoteDataSource;
    private FavoriteLocalDataSource favoriteLocalDataSource;

    private List<String> favoriteMealIds = new ArrayList<>();
    SharedPreferanceLocalDataSource sharedPreferences;

    public HomePresenterImp(HomeView view,
                            MealRemoteDataSource remoteDataSource,
                            FavoriteLocalDataSource favoriteLocalDataSource,
                            SharedPreferanceLocalDataSource sharedPreferences) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
        this.favoriteLocalDataSource = favoriteLocalDataSource;
        this.sharedPreferences = sharedPreferences;
        loadFavoriteIds();
    }

    private void loadFavoriteIds() {
        favoriteLocalDataSource.getFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favorites -> {
                    // if the user is changed
                    favoriteMealIds.clear();
                    for (FavoriteEntity f : favorites) {
                        // store it again
                        favoriteMealIds.add(f.idMeal);
                    }
                    if (view != null) {
                        view.updateFavoriteIds(favoriteMealIds);
                    }
                });
    }

    @Override
    public void getCategoryList() {
        remoteDataSource.getCategoryList(new MealNetworkResponse<Category>() {
            @Override
            public void onSuccess(List<Category> dataList) {
                if (view != null) view.onGetCategorySuccess(dataList);
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) view.onFailure(errorMessage);
            }

            @Override
            public void noInternet() {
                if (view != null) view.onNoInternet();
            }
        });
    }

    @Override
    public void getPopularList() {
        remoteDataSource.getPopularList(new MealNetworkResponse<Meal>() {
            @Override
            public void onSuccess(List<Meal> dataList) {
                if (view != null) {
                    view.onGetPopularSuccess(dataList);
                    view.updateFavoriteIds(favoriteMealIds);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) view.onFailure(errorMessage);
            }

            @Override
            public void noInternet() {
                if (view != null) view.onNoInternet();
            }
        });
    }

    @Override
    public void getMealOfDay() {
        remoteDataSource.getMealOfDay(new MealNetworkResponse<Meal>() {
            @Override
            public void onSuccess(List<Meal> dataList) {
                if (view != null) view.onGetMealOfDaySuccess(dataList);
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) view.onFailure(errorMessage);
            }

            @Override
            public void noInternet() {
                if (view != null) view.onNoInternet();
            }
        });
    }
    @Override
    public void addToFavorite(Meal meal) {
        if (meal == null) return;
        FavoriteEntity favorite = new FavoriteEntity(
                meal.getIdMeal(),
               sharedPreferences.getUserId(),
                meal.getStrMeal(),
                meal.getStrMealThumb(),
                meal.getStrCategory(),
                meal.getStrArea(),
                meal.getStrTags(),
                meal.getStrYoutube(),
                meal.getStrInstructions(),
                meal.getIngredientsList().toArray(new String[0]), meal.getMeasuresList().toArray(new String[0])
        );
        favoriteLocalDataSource.insertFavoriteMeal(favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (!favoriteMealIds.contains(meal.getIdMeal())) {
                        favoriteMealIds.add(meal.getIdMeal());
                        if (view != null) view.updateFavoriteIds(favoriteMealIds);
                    }
                });
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        if (meal == null) return;
        FavoriteEntity favorite = new FavoriteEntity();
        favoriteLocalDataSource.deleteFavoriteMeal(favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    favoriteMealIds.remove(meal.getIdMeal());
                    if (view != null) view.updateFavoriteIds(favoriteMealIds);
                }, throwable -> {});
    }
    @Override
    public List<String> getFavoriteMealIds() {
        return new ArrayList<>(favoriteMealIds);
    }
}
