package com.example.mealplanner.presentation.account.view;

public interface AccountView {
    void navigateToLogin();
    void showLogoutError(String message);
    void showSyncSuccess();
    void showSyncError(String error);
}