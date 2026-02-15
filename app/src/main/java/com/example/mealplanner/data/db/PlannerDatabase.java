package com.example.mealplanner.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.mealplanner.datasource.plan.local.PlanDao;
import com.example.mealplanner.data.enitiy.PlanEntity;

@Database(entities = {PlanEntity.class}, version = 3, exportSchema = false)
public abstract class PlannerDatabase extends RoomDatabase {

    public abstract PlanDao planDao();
    private static volatile PlannerDatabase INSTANCE;
    public static PlannerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PlannerDatabase.class,
                                    "planDB"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}