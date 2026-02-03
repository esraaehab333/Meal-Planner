package com.example.mealplanner.repository;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.auth.remote.AuthNetworkResponse;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;

public class AuthRepository {

    private SharedPreferanceLocalDataSource localDataSource;
    private AuthRemoteDataSource remoteDataSource;

    public AuthRepository(SharedPreferanceLocalDataSource localDataSource, AuthRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    // 🔹 Login with email
    public void login(String email, String password, AuthNetworkResponse callback) {
        remoteDataSource.login(email, password, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                //localDataSource.saveUserEmail(email);
                callback.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    // 🔹 Register
    public void register(String email,String username ,String password, AuthNetworkResponse callback) {
        remoteDataSource.register(email,username ,password, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                //localDataSource.saveUserEmail(email);
                callback.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void loginWithGoogle(String idToken, AuthNetworkResponse callback) {
        remoteDataSource.loginWithGoogle(idToken, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void loginWithFacebook(String accessToken, AuthNetworkResponse callback) {
        remoteDataSource.loginWithFacebook(accessToken, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public void logout() {
        remoteDataSource.logout();
        //localDataSource.clearUserData();
    }

}
