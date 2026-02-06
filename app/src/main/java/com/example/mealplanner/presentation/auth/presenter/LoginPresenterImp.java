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
        if(!isValidEmail(email)){
            authView.onError("Invalid email");
            return;
        }

        if(!isValidPassword(password)){
            authView.onError("Password must be at least 6 characters");
            return;
        }

        authView.showLoading();
        remoteDataSource.login(email, password, new AuthNetworkResponse() {
            @Override
            public void onSuccess() {
                authView.hideLoading();
                authView.onSuccess("Login successful");
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
    private boolean isValidEmail(String email){
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidPassword(String password){
        return password != null && password.length() >= 6;
    }
}
