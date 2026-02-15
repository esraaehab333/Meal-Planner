package com.example.mealplanner.datasource.favorite.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealplanner.data.enitiy.FavoriteEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteEntity favorite);
    @Query("SELECT * FROM favorite_meals WHERE userId = :userId")
    Flowable<List<FavoriteEntity>> getFavoritesByUser(String userId);
    @Query("DELETE FROM favorite_meals WHERE idMeal = :mealId AND userId = :userId")
    void deleteFavorite(String mealId, String userId);
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE idMeal = :mealId AND userId = :userId)")
    boolean isFavorite(String mealId, String userId);
    @Query("DELETE FROM favorite_meals WHERE userId = :userId")
    void deleteAllFavoritesForUser(String userId);
    @Query("SELECT idMeal FROM favorite_meals")
    List<String> getFavoriteMealIds();
}