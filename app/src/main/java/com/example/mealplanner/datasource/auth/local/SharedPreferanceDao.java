package com.example.mealplanner.datasource.auth.local;

public interface SharedPreferanceDao {
    void saveUserId(String userId);
    String getUserId();
    void clearUserData();
    void setOnboardingCompleted(boolean isCompleted);
    boolean isOnboardingCompleted();
}