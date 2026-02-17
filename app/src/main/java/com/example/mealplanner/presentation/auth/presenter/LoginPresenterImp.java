package com.example.mealplanner.presentation.auth.presenter;

import android.app.Application;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;
import com.example.mealplanner.presentation.auth.view.AuthView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImp implements LoginPresenter {

    private AuthView authView;
    //private AuthRemoteDataSource remoteDataSource;
    //private SharedPreferanceDao sharedPrefDao;
    private CompositeDisposable compositeDisposable;
    private AuthRepository authRepository;

    public LoginPresenterImp(Application application, AuthView view) {
        this.authRepository = new AuthRepositoryImpl(application);
        this.authView = view;
       // this.sharedPrefDao = sharedPrefDao;
       // this.remoteDataSource = new AuthRemoteDataSource();
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(String email, String password) {
        authView.showLoading();
        compositeDisposable.add(
                authRepository.login(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    authRepository.saveUserId(userModel.getUid());
                                    authRepository.saveUserName(userModel.getUsername());
                                    authRepository.saveUserEmail(userModel.getEmail());
                                    authView.hideLoading();
                                    authView.onSuccess("Welcome " + userModel.getUsername());
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
        authView.showLoading();
        compositeDisposable.add(
                authRepository.loginWithGoogle(idToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    authRepository.saveUserId(userModel.getUid());
                                    authRepository.saveUserName(userModel.getUsername());
                                    authRepository.saveUserEmail(userModel.getEmail());
                                    authView.hideLoading();
                                    authView.onSuccess("Google Login Success");
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
                authRepository.loginWithFacebook(accessToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    authRepository.saveUserId(userModel.getUid());
                                    authRepository.saveUserName(userModel.getUsername());
                                    authRepository.saveUserEmail(userModel.getEmail());

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
        authRepository.saveUserId("GUEST");
        authView.onSuccess("Logged in as Guest");
    }
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}