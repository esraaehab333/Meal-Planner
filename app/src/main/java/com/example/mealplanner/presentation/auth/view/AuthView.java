package com.example.mealplanner.presentation.auth.view;

public interface AuthView {
    void showLoading();

    void hideLoading();

    void onSuccess(String message);

    void onError(String errorMessage);
}
