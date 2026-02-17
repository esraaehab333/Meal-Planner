package com.example.mealplanner.datasource.reposatory;

import android.app.Application;

import com.example.mealplanner.data.models.UserModel;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;

import io.reactivex.rxjava3.core.Single;

public class AuthRepositoryImpl implements AuthRepository{
    private SharedPreferanceLocalDataSource sharedPreferanceLocalDataSource;
    private AuthRemoteDataSource authRemoteDataSource;

    public AuthRepositoryImpl(Application application){
        authRemoteDataSource = new AuthRemoteDataSource();
        sharedPreferanceLocalDataSource = new SharedPreferanceLocalDataSource(application);
    }

    @Override
    public Single<UserModel> login(String email, String password) {
        return authRemoteDataSource.login(email,password);
    }

    @Override
    public Single<UserModel> register(String email, String username, String password) {
        return authRemoteDataSource.register(email,username,password);
    }

    @Override
    public Single<UserModel> loginWithGoogle(String idToken) {
        return authRemoteDataSource.loginWithGoogle(idToken);
    }

    @Override
    public Single<UserModel> loginWithFacebook(String accessToken) {
        return authRemoteDataSource.loginWithFacebook(accessToken);
    }

    @Override
    public Single<UserModel> getUserFromFirestore(String uid) {
        return authRemoteDataSource.getUserFromFirestore(uid);
    }

    @Override
    public void logout() {
        authRemoteDataSource.logout();
    }

    @Override
    public boolean isOnboardingCompleted() {
        return sharedPreferanceLocalDataSource.isOnboardingCompleted();
    }

    @Override
    public void clearUserData() {
        sharedPreferanceLocalDataSource.clearUserData();
    }

    @Override
    public String getUserEmail() {
        return sharedPreferanceLocalDataSource.getUserEmail();
    }

    @Override
    public String getUserName() {
        return sharedPreferanceLocalDataSource.getUserName();
    }

    @Override
    public String getUserId() {
        return sharedPreferanceLocalDataSource.getUserId();
    }

    @Override
    public void saveUserEmail(String email) {
        sharedPreferanceLocalDataSource.saveUserEmail(email);
    }

    @Override
    public void saveUserName(String name) {
        sharedPreferanceLocalDataSource.saveUserName(name);
    }

    @Override
    public void saveUserId(String userId) {
        sharedPreferanceLocalDataSource.saveUserId(userId);
    }
}
