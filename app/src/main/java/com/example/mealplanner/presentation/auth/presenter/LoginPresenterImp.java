package com.example.mealplanner.presentation.auth.presenter;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;
import com.example.mealplanner.presentation.auth.view.AuthView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImp implements LoginPresenter {

    private AuthView authView;
    private AuthRemoteDataSource remoteDataSource;
    private SharedPreferanceDao sharedPrefDao;
    private CompositeDisposable compositeDisposable;

    public LoginPresenterImp(AuthView authView, SharedPreferanceDao sharedPrefDao) {
        this.authView = authView;
        this.sharedPrefDao = sharedPrefDao;
        this.remoteDataSource = new AuthRemoteDataSource();
        this.compositeDisposable = new CompositeDisposable();
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
        compositeDisposable.add(
                remoteDataSource.login(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userId -> {
                                    sharedPrefDao.saveUserId(userId);
                                    authView.hideLoading();
                                    authView.onSuccess("Welcome back!");
                                },
                                error -> {
                                    authView.hideLoading();
                                    authView.onError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loginWithGoogle(String idToken) {
        if (idToken == null || idToken.isEmpty()) {
            authView.onError("Google authentication failed: Missing Token");
            return;
        }

        authView.showLoading();
        compositeDisposable.add(
                remoteDataSource.loginWithGoogle(idToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userId -> {
                                    sharedPrefDao.saveUserId(userId);
                                    authView.hideLoading();
                                    authView.onSuccess("Google Login Successful");
                                },
                                error -> {
                                    authView.hideLoading();
                                    authView.onError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loginWithFacebook(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            authView.onError("Facebook authentication failed: Missing Token");
            return;
        }

        authView.showLoading();
        compositeDisposable.add(
                remoteDataSource.loginWithFacebook(accessToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userId -> {
                                    sharedPrefDao.saveUserId(userId);
                                    authView.hideLoading();
                                    authView.onSuccess("Facebook Login Successful");
                                },
                                error -> {
                                    authView.hideLoading();
                                    authView.onError(error.getMessage());
                                }
                        )
        );
    }
    @Override
    public void loginAsGuest() {
        sharedPrefDao.saveUserId("GUEST");
        authView.onSuccess("Logged in as Guest");
    }
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}