package com.example.mealplanner.datasource.auth.remote;
public interface AuthNetworkResponse {
    void onSuccess(String userId);
    void onFailure(String errorMessage);
}