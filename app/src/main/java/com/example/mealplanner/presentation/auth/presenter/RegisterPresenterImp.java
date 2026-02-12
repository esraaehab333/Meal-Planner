package com.example.mealplanner.presentation.auth.presenter;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.remote.AuthRemoteDataSource;
import com.example.mealplanner.presentation.auth.view.AuthView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenterImp implements RegisterPresenter {

    private AuthView authView;
    private AuthRemoteDataSource remoteDataSource;
    private SharedPreferanceDao sharedPrefDao;
    private CompositeDisposable compositeDisposable;

    public RegisterPresenterImp(AuthView authView, SharedPreferanceDao sharedPrefDao) {
        this.authView = authView;
        this.sharedPrefDao = sharedPrefDao;
        this.remoteDataSource = new AuthRemoteDataSource();
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
                remoteDataSource.register(email, username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                uid -> {
                                    sharedPrefDao.saveUserId(uid);
                                    authView.hideLoading();
                                    authView.onSuccess("Registration successful");
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

    // Add this method to clean up subscriptions when the presenter is destroyed
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}