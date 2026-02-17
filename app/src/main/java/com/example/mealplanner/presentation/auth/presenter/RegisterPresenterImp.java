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

public class RegisterPresenterImp implements RegisterPresenter {

    private AuthView authView;
    //private AuthRemoteDataSource remoteDataSource;
   //private SharedPreferanceDao sharedPrefDao;
    private CompositeDisposable compositeDisposable;
    private AuthRepository authRepository;

    public RegisterPresenterImp(AuthView authView, Application application) {
        this.authView = authView;
        this.authRepository = new AuthRepositoryImpl(application);
       // this.sharedPrefDao = sharedPrefDao;
       // this.remoteDataSource = new AuthRemoteDataSource();
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void register(String email, String username, String password) {
        if (username.isEmpty()) {
            authView.onError("VALIDATION_USERNAME_EMPTY");
            return;
        }

        if (!isValidEmail(email)) {
            authView.onError("VALIDATION_EMAIL_INVALID");
            return;
        }

        if (!isValidPassword(password)) {
            authView.onError("VALIDATION_PASSWORD_SHORT");
            return;
        }
        authView.showLoading();
        compositeDisposable.add(
                authRepository.register(email, username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    authRepository.saveUserId(userModel.getUid());
                                    authRepository.saveUserName(userModel.getUsername());
                                    authRepository.saveUserEmail(userModel.getEmail());
                                    authView.hideLoading();
                                    authView.onSuccess("Success!");
                                },
                                error -> {
                                    authView.hideLoading();
                                    authView.onError(error.getMessage());
                                }
                        )
        );
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}