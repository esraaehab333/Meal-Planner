package com.example.mealplanner.presentation.meal.presenter;

import android.app.Application;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.datasource.reposatory.MealRepositoryImpl;
import com.example.mealplanner.presentation.meal.view.MealView;
import com.example.mealplanner.utils.mapper.FavoriteMapper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPresenterImp implements MealPresenter {

    private MealView view;
    private MealRepository mealRepository;
    private AuthRepository authRepository;
    private String currentUserId;
    private CompositeDisposable compositeDisposable;

    public MealPresenterImp(MealView view, Application application) {
        this.view = view;
        this.authRepository = new AuthRepositoryImpl(application);
        this.currentUserId = authRepository.getUserId();
        this.mealRepository = new MealRepositoryImpl(application, currentUserId);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadMeal(Meal meal) {
        if (view != null) {
            view.showMeal(meal);
            isFavorite(meal.getIdMeal());
        }
    }

    @Override
    public void loadMealById(String mealId) {
        if (view != null) {
            view.showLoading();
        }
        compositeDisposable.add(
                mealRepository.getMealById(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (view != null && meals != null && !meals.isEmpty()) {
                                        view.hideLoading();
                                        Meal meal = meals.get(0);
                                        view.showMeal(meal);
                                        isFavorite(meal.getIdMeal());
                                    } else {
                                        if (view != null) {
                                            view.hideLoading();
                                            view.showErrorMessage("Meal not found");
                                        }
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showErrorMessage("Failed to load meal: " + throwable.getMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void addToFav(Meal meal) {
        FavoriteEntity entity = FavoriteMapper.fromMeal(meal, currentUserId);

        compositeDisposable.add(
                mealRepository.insertFavoriteMeal(entity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (view != null) {
                                        view.updateFavoriteStatus(true);
                                        view.showSuccessMessage("Added to favorites");
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showErrorMessage("Failed to add to favorites: " + throwable.getMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void deleteFromFav(Meal meal) {
        FavoriteEntity entity = FavoriteMapper.fromMeal(meal, currentUserId);

        compositeDisposable.add(
                mealRepository.deleteFavoriteMeal(entity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (view != null) {
                                        view.updateFavoriteStatus(false);
                                        view.showSuccessMessage("Removed from favorites");
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showErrorMessage("Failed to remove from favorites: " + throwable.getMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void isFavorite(String mealId) {
        compositeDisposable.add(
                mealRepository.isFavorite(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isFav -> {
                                    if (view != null) {
                                        view.updateFavoriteStatus(isFav);
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.updateFavoriteStatus(false);
                                    }
                                }
                        )
        );
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        view = null;
    }
}