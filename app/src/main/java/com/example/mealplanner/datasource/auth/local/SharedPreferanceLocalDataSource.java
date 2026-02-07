package com.example.mealplanner.datasource.auth.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferanceLocalDataSource implements SharedPreferanceDao {

    private static final String PREF_NAME = "MealPlannerPrefs";
    private static final String KEY_USER_ID = "USER_ID";
    private final SharedPreferences sharedPreferences;

    public SharedPreferanceLocalDataSource(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveUserId(String userId) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    @Override
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    @Override
    public void clearUserData() {
        sharedPreferences.edit().clear().apply();
    }
}