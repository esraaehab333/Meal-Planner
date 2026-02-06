package com.example.mealplanner.presentation.auth.presenter;

import com.example.mealplanner.datasource.auth.remote.AuthNetworkResponse;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;
import com.example.mealplanner.presentation.auth.view.AuthView;

public class LoginPresenterImp implements LoginPresenter {

    private AuthView authView;
    private AuthRemoteDataSource remoteDataSource;
    public LoginPresenterImp(AuthView authView) {
        this.authView = authView;
        this.remoteDataSource = new AuthRemoteDataSource();
    }
    @Override
    public void login(String email, String password) {
        if (email.isEmpty()) {
            authView.onError("VALIDATION_EMAIL_EMPTY");
            return;
        }
        if (password.isEmpty()) {
            authView.onError("VALIDATION_PASSWORD_EMPTY");
            return;
        }

        authView.showLoading();
        remoteDataSource.login(email, password, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                authView.hideLoading();
                authView.onSuccess("Welcome back!");
            }

            @Override
            public void onFailure(String errorMessage) {
                authView.hideLoading();
                authView.onError(errorMessage);
            }
        });
    }
    @Override
    public void loginWithGoogle(String idToken) {
        if (idToken == null || idToken.isEmpty()) {
            authView.onError("Google authentication failed: Missing Token");
            return;
        }

        authView.showLoading();
        remoteDataSource.loginWithGoogle(idToken, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                authView.hideLoading();
                authView.onSuccess("Google Login Successful");
            }

            @Override
            public void onFailure(String errorMessage) {
                authView.hideLoading();
                authView.onError(errorMessage);
            }
        });
    }

    @Override
    public void loginWithFacebook(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            authView.onError("Facebook authentication failed: Missing Token");
            return;
        }

        authView.showLoading();
        remoteDataSource.loginWithFacebook(accessToken, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                authView.hideLoading();
                authView.onSuccess("Facebook Login Successful");
            }

            @Override
            public void onFailure(String errorMessage) {
                authView.hideLoading();
                authView.onError(errorMessage);
            }
        });
    }
    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}