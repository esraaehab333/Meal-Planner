package com.example.mealplanner.datasource.auth.local;

public interface SharedPreferanceDao {
    void saveUserId(String userId);
    void saveUserName(String name);
    void saveUserEmail(String email);

    String getUserId();
    String getUserName();
    String getUserEmail();

    void clearUserData();
    boolean isOnboardingCompleted();
}