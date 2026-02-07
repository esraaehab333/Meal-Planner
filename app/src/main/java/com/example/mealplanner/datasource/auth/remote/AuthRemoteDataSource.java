package com.example.mealplanner.datasource.auth.remote;

import com.example.mealplanner.data.network.AuthService;

public class AuthRemoteDataSource {
    private AuthService authService;

    public AuthRemoteDataSource() {
        authService = new AuthService();
    }

    public void login(String email, String password, AuthNetworkResponse callback) {
        authService.login(email, password, new AuthNetworkResponse() {
            @Override
            public void onSuccess(String userId) {
                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void register(String email, String username, String password, AuthNetworkResponse callback) {
        authService.register(email, username, password, new AuthNetworkResponse() {
            @Override
            public void onSuccess(String userId) {
                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void loginWithGoogle(String idToken, AuthNetworkResponse callback) {
        authService.loginWithGoogle(idToken, new AuthNetworkResponse() {
            @Override
            public void onSuccess(String userId) {
                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void loginWithFacebook(String accessToken, AuthNetworkResponse callback) {
        authService.loginWithFacebook(accessToken, new AuthNetworkResponse() {
            @Override
            public void onSuccess(String userId) {
                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void logout() {
        authService.logout();
    }
}
