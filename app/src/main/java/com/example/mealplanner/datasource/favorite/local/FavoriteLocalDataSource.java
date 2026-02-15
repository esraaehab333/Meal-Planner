package com.example.mealplanner.datasource.favorite.local;

import android.content.Context;

import com.example.mealplanner.data.db.AppDatabase;
import com.example.mealplanner.data.enitiy.FavoriteEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class FavoriteLocalDataSource {
    private FavoriteDao favoriteDao;
    private String currentUserId;

    public FavoriteLocalDataSource(Context context, String userId) {
        this.favoriteDao = AppDatabase.getInstance(context).favoriteDao();
        this.currentUserId = userId;
    }

    public Flowable<List<FavoriteEntity>> getFavoriteMeals() {
        return favoriteDao.getFavoritesByUser(currentUserId);
    }

    public Completable insertFavoriteMeal(FavoriteEntity favorite) {
        return Completable.fromAction(() -> favoriteDao.insertFavorite(favorite));
    }

    public Completable deleteFavoriteMeal(FavoriteEntity favorite) {
        return Completable.fromAction(() ->
                favoriteDao.deleteFavorite(favorite.idMeal, currentUserId));
    }

    public Single<Boolean> isFavorite(String mealId) {
        return Single.fromCallable(() ->
                favoriteDao.isFavorite(mealId, currentUserId));
    }

    public Completable deleteAllFavorites() {
        return Completable.fromAction(() ->
                favoriteDao.deleteAllFavoritesForUser(currentUserId));
    }
}