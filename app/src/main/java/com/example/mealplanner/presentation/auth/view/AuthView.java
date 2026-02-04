package com.example.mealplanner.presentation.auth.view;

import android.content.Context;

// AuthView.java
public interface AuthView {
    void showLoading();

    void hideLoading();

    void onSuccess(String message);

    void onError(String errorMessage);
}
