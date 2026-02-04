package com.example.mealplanner.datasource.auth.remote;
// working as callback to dealing change ui
public interface AuthNetworkResponse {
    void onSuccess();
    void onFailure(String errorMessage);
}
