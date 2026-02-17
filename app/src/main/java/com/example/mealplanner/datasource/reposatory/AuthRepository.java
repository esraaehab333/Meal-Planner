package com.example.mealplanner.datasource.reposatory;

import com.example.mealplanner.data.models.UserModel;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    public Single<UserModel> login(String email, String password);
    public Single<UserModel> register(String email, String username, String password);
    public Single<UserModel> loginWithGoogle(String idToken);
    public Single<UserModel> loginWithFacebook(String accessToken);
    public Single<UserModel> getUserFromFirestore(String uid);
    public void logout();
    public boolean isOnboardingCompleted();
    public void clearUserData();
    public String getUserEmail();
    public String getUserName();
    public String getUserId();
    public void saveUserEmail(String email);
    public void saveUserName(String name);
    public void saveUserId(String userId);
}
