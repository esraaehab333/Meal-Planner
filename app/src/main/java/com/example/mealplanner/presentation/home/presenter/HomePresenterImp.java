package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.presentation.home.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImp implements HomePresenter {

    private HomeView view;
    private MealRemoteDataSource remoteDataSource;
    private FavoriteLocalDataSource favoriteLocalDataSource;
    private SharedPreferanceLocalDataSource sharedPreferences;

    private List<String> favoriteMealIds = new ArrayList<>();

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
                    favoriteMealIds.clear();
                    for (FavoriteEntity f : favorites) {
                        favoriteMealIds.add(f.idMeal);
                    }
                    if (view != null) {
                        view.updateFavoriteIds(favoriteMealIds);
                    }
                }, throwable -> {
                    if (view != null) view.onFailure(throwable.getMessage());
                });
    }

    @Override
    public void getCategoryList() {
        remoteDataSource.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    if (view != null) view.onGetCategorySuccess(categories);
                }, throwable -> {
                    if (view != null) view.onFailure(throwable.getMessage());
                });
    }

    @Override
    public void getPopularList() {
        remoteDataSource.getPopularList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    if (view != null) {
                        view.onGetPopularSuccess(meals);
                        view.updateFavoriteIds(favoriteMealIds);
                    }
                }, throwable -> {
                    if (view != null) view.onFailure(throwable.getMessage());
                });
    }

    @Override
    public void getMealOfDay() {
        remoteDataSource.getMealOfDay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    if (view != null) view.onGetMealOfDaySuccess(meals);
                }, throwable -> {
                    if (view != null) view.onFailure(throwable.getMessage());
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
                meal.getIngredientsList().toArray(new String[0]),
                meal.getMeasuresList().toArray(new String[0])
        );

        favoriteLocalDataSource.insertFavoriteMeal(favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (!favoriteMealIds.contains(meal.getIdMeal())) {
                        favoriteMealIds.add(meal.getIdMeal());
                        if (view != null) view.updateFavoriteIds(favoriteMealIds);
                    }
                }, throwable -> {
                    if (view != null) view.onFailure(throwable.getMessage());
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
                }, throwable -> {
                    if (view != null) view.onFailure(throwable.getMessage());
                });
    }

    @Override
    public List<String> getFavoriteMealIds() {
        return new ArrayList<>(favoriteMealIds);
    }
}
