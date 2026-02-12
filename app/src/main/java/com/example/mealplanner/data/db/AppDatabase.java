package com.example.mealplanner.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mealplanner.datasource.favorite.local.FavoriteDao;
import com.example.mealplanner.data.enitiy.FavoriteEntity;

@Database(entities = {FavoriteEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
    private static AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "mealsDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}