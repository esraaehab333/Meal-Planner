package com.example.mealplanner.datasource.auth.remote;

import com.example.mealplanner.data.network.AuthService;
import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSource {
    private AuthService authService;

    public AuthRemoteDataSource() {
        authService = new AuthService();
    }

    public Single<String> login(String email, String password) {
        return authService.login(email, password);
    }

    public Single<String> register(String email, String username, String password) {
        return authService.register(email, username, password);
    }

    public Single<String> loginWithGoogle(String idToken) {
        return authService.loginWithGoogle(idToken);
    }

    public Single<String> loginWithFacebook(String accessToken) {
        return authService.loginWithFacebook(accessToken);
    }

    public void logout() {
        authService.logout();
    }
}