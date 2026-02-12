package com.example.mealplanner.datasource.plan.local;

import android.content.Context;
import com.example.mealplanner.data.db.PlannerDatabase;
import com.example.mealplanner.data.enitiy.PlanEntity;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class PlanLocalDataSource {
    private final PlanDao planDao;
    private String currentUserId;
    public PlanLocalDataSource(Context context, String userId) {
        this.planDao = PlannerDatabase.getInstance(context.getApplicationContext()).planDao();
        this.currentUserId = userId;
    }
    public Flowable<List<PlanEntity>> getAllPlannedMeals() {
        return planDao.getAllPlannedMeals(currentUserId);
    }
    public Flowable<List<PlanEntity>> getMealsByDate(String selectedDate) {
        return planDao.getMealsByDate(selectedDate, currentUserId);
    }
    public Completable insertMealToPlan(PlanEntity plan) {
        return Completable.fromAction(() ->
                planDao.insertMealToPlan(plan));
    }
    public Completable deleteMealFromPlan(PlanEntity plan) {
        return Completable.fromAction(() ->
                planDao.deleteMealFromPlan(plan));
    }
}