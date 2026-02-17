package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.presentation.home.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImp implements HomePresenter {

    private HomeView view;
    private MealRepository mealRepository;
    private AuthRepository authRepository;

    private List<String> favoriteMealIds = new ArrayList<>();

    public HomePresenterImp(HomeView view,
                            MealRepository mealRepository,
                            AuthRepository authRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
        this.authRepository = authRepository;
        loadFavoriteIds();
    }

    private void loadFavoriteIds() {
        mealRepository.getFavoriteMeals()
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
        mealRepository.getCategoriesList()
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
        mealRepository.getPopularList()
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
        mealRepository.getMealOfDay()
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
                authRepository.getUserId(),
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

        mealRepository.insertFavoriteMeal(favorite)
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

        FavoriteEntity favorite = new FavoriteEntity(
                meal.getIdMeal(),
                authRepository.getUserId(),
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

        mealRepository.deleteFavoriteMeal(favorite)
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
