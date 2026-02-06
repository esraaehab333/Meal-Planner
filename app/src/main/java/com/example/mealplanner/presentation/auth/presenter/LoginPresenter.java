package com.example.mealplanner.presentation.auth.presenter;

public interface LoginPresenter {
    void login(String email, String password);
    void loginWithGoogle(String idToken);
    void loginWithFacebook(String accessToken);
}