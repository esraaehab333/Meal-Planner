package com.example.mealplanner.presentation.meal.presenter;

import android.content.Context;

import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.utils.mapper.FavoriteMapper;
import com.example.mealplanner.presentation.meal.view.MealView;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPresenterImp implements MealPresenter {

    private MealView view;
    private FavoriteLocalDataSource localDataSource;
    private String currentUserId;
    private CompositeDisposable compositeDisposable;

    public MealPresenterImp(MealView view, Context context) {
        this.view = view;
        this.currentUserId = getCurrentUserId();
        this.localDataSource = new FavoriteLocalDataSource(context, currentUserId);
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
    public void addToFav(Meal meal) {
        FavoriteEntity entity = FavoriteMapper.fromMeal(meal, currentUserId);

        compositeDisposable.add(
                localDataSource.insertFavoriteMeal(entity)
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
                localDataSource.deleteFavoriteMeal(entity)
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
                localDataSource.isFavorite(mealId)
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

    private String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return "GUEST";
    }
}