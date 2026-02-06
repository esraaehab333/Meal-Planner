package com.example.mealplanner.presentation.auth.presenter;

import com.example.mealplanner.datasource.auth.remote.AuthNetworkResponse;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;
import com.example.mealplanner.presentation.auth.view.AuthView;

public class RegisterPresenterImp implements RegisterPresenter {

    private AuthView authView;
    private AuthRemoteDataSource remoteDataSource;
    public RegisterPresenterImp(AuthView authView) {
        this.authView = authView;
        this.remoteDataSource = new AuthRemoteDataSource();
    }

    @Override
    public void register(String email, String username, String password) {
        if (username.isEmpty()) {
            authView.onError("VALIDATION_USERNAME_EMPTY");
            return;
        }

        if (!isValidEmail(email)) {
            authView.onError("VALIDATION_EMAIL_INVALID");
            return;
        }

        if (!isValidPassword(password)) {
            authView.onError("VALIDATION_PASSWORD_SHORT");
            return;
        }

        authView.showLoading();
        remoteDataSource.register(email, username, password, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                authView.hideLoading();
                authView.onSuccess("Registration successful");
            }

            @Override
            public void onFailure(String errorMessage) {
                authView.hideLoading();
                authView.onError(errorMessage);
            }
        });
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}