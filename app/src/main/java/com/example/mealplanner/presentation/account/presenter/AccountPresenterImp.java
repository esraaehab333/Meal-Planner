package com.example.mealplanner.presentation.account.presenter;

import android.content.Context;

import com.example.mealplanner.data.network.AuthService;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.presentation.account.view.AccountView;

public class AccountPresenterImp implements AccountPresenter {
    private AccountView view;
    private AuthService authService;
    private SharedPreferanceLocalDataSource sharedPref;

    public AccountPresenterImp(AccountView view, Context context) {
        this.view = view;
        this.authService = new AuthService();
        this.sharedPref = new SharedPreferanceLocalDataSource(context);
    }
    @Override
    public void onLogoutClicked() {
        String currentId = sharedPref.getUserId();

        if ("GUEST".equals(currentId)) {
            // GUEST CASE: Just clear the local "GUEST" flag
            sharedPref.clearUserData();
            view.navigateToLogin();
        } else {
            // REGISTERED USER CASE: Clear Firebase + Local Data
            authService.logout();
            sharedPref.clearUserData();
            view.navigateToLogin();
        }
    }
}