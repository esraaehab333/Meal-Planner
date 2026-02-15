package com.example.mealplanner.datasource.auth.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferanceLocalDataSource implements SharedPreferanceDao {

    private static final String PREF_NAME = "MealPlannerPrefs";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";

    private final SharedPreferences sharedPreferences;

    public SharedPreferanceLocalDataSource(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveUserId(String userId) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    @Override
    public void saveUserName(String name) {
        sharedPreferences.edit().putString(KEY_USER_NAME, name).apply();
    }

    @Override
    public void saveUserEmail(String email) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    @Override
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    @Override
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "Guest User");
    }

    @Override
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "No Email Found");
    }

    @Override
    public void clearUserData() {
        sharedPreferences.edit().clear().apply();
    }

    @Override
    public boolean isOnboardingCompleted() {
        return sharedPreferences.getBoolean("IS_ONBOARDING_COMPLETED", false);
    }
}