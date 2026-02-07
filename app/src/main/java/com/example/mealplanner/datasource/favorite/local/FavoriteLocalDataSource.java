package com.example.mealplanner.datasource.favorite.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mealplanner.data.db.AppDatabase;
import com.example.mealplanner.models.FavoriteEntity;

import java.util.List;

public class FavoriteLocalDataSource {
    private FavoriteDao favoriteDao;
    private String currentUserId;
    public FavoriteLocalDataSource(Context context, String userId) {
        this.favoriteDao = AppDatabase.getInstance(context).favoriteDao();
        this.currentUserId = userId;
    }
    public LiveData<List<FavoriteEntity>> getFavoriteMeals() {
        return favoriteDao.getFavoritesByUser(currentUserId);
    }
    public void insertFavoriteMeal(FavoriteEntity favorite) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoriteDao.insertFavorite(favorite);
            }
        }).start();
    }
    public void deleteFavoriteMeal(FavoriteEntity favorite) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoriteDao.deleteFavorite(favorite.idMeal, currentUserId);
            }
        }).start();
    }
    public void isFavorite(String mealId, FavoriteCheckCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isFav = favoriteDao.isFavorite(mealId, currentUserId);
                if (callback != null) {
                    callback.onResult(isFav);
                }
            }
        }).start();
    }
    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }
    public interface FavoriteCheckCallback {
        void onResult(boolean isFavorite);
    }
}