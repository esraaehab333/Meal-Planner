package com.example.mealplanner.presentation.account.presenter;

import android.app.Application;
import android.util.Log;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.datasource.reposatory.MealRepositoryImpl;
import com.example.mealplanner.presentation.account.view.AccountView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountPresenterImp implements AccountPresenter {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final AccountView view;
    private final AuthRepository authRepository;
    private final MealRepository mealRepository;
    private final FirebaseFirestore db;

    public AccountPresenterImp(AccountView view, Application application) {
        this.view = view;
        this.authRepository = new AuthRepositoryImpl(application);
        String userId = authRepository.getUserId();
        this.mealRepository = new MealRepositoryImpl(application, userId);
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void loadCounts() {
        disposables.add(
                mealRepository.getFavoriteMeals()
                        .take(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                list -> view.showFavoriteCount(list != null ? list.size() : 0),
                                throwable -> view.showFavoriteCount(0)
                        )
        );
        disposables.add(
                mealRepository.getAllPlannedMeals()
                        .take(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                list -> view.showPlannedCount(list != null ? list.size() : 0),
                                throwable -> view.showPlannedCount(0)
                        )
        );
    }

    @Override
    public void onSyncClicked() {
        String userId = authRepository.getUserId();

        if ("GUEST".equals(userId)) {
            view.showSyncError("Please login to sync your data!");
            return;
        }
        db.collection("users").document(userId).collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().delete();
                    }
                    uploadFavoritesFromLocal(userId);
                });
        db.collection("users").document(userId).collection("plan")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().delete();
                    }
                    uploadPlanFromLocal(userId);
                });
    }

    private void uploadFavoritesFromLocal(String userId) {
        mealRepository.getFavoriteMeals()
                .take(1)
                .subscribe(favorites -> {
                    for (FavoriteEntity fav : favorites) {
                        db.collection("users").document(userId)
                                .collection("favorites").document(fav.idMeal).set(fav);
                    }
                }, throwable -> view.showSyncError(throwable.getMessage()));
    }

    private void uploadPlanFromLocal(String userId) {
        mealRepository.getAllPlannedMeals()
                .take(1)
                .subscribe(plans -> {
                    for (PlanEntity plan : plans) {
                        db.collection("users").document(userId)
                                .collection("plan")
                                .document(plan.idMeal + "_" + plan.date).set(plan);
                    }
                    view.showSyncSuccess();
                }, throwable -> view.showSyncError(throwable.getMessage()));
    }

    @Override
    public void onLogoutClicked() {
        String currentId = authRepository.getUserId();
        mealRepository.deleteAllFavorites()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> Log.d("LOGOUT", "Favorites deleted"),
                        throwable -> Log.e("LOGOUT", "Error deleting favorites"));

        mealRepository.deleteAllPlans()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> Log.d("LOGOUT", "Plans deleted"),
                        throwable -> Log.e("LOGOUT", "Error deleting plans"));

        if ("GUEST".equals(currentId)) {
            authRepository.clearUserData();
            view.navigateToLogin();
        } else {
            authRepository.logout();
            authRepository.clearUserData();
            view.navigateToLogin();
        }
    }
}