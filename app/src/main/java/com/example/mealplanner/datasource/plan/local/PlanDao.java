package com.example.mealplanner.datasource.plan.local;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.mealplanner.models.PlanEntity;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealToPlan(PlanEntity plan);

    @Query("SELECT * FROM plan_meals WHERE userId = :userId")
    Flowable<List<PlanEntity>> getAllPlannedMeals(String userId);

    @Query("SELECT * FROM plan_meals WHERE date = :selectedDate AND userId = :userId")
    Flowable<List<PlanEntity>> getMealsByDate(String selectedDate, String userId);

    @Delete
    void deleteMealFromPlan(PlanEntity plan);

}