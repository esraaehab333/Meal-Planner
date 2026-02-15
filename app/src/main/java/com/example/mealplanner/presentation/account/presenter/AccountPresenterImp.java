package com.example.mealplanner.presentation.account.presenter;

import android.content.Context;
import android.util.Log;

import com.example.mealplanner.data.enitiy.FavoriteEntity;
import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.data.network.AuthService;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.datasource.plan.local.PlanLocalDataSource;
import com.example.mealplanner.presentation.account.view.AccountView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountPresenterImp implements AccountPresenter {
    private AccountView view;
    private AuthService authService;
    private SharedPreferanceLocalDataSource sharedPref;
    private FavoriteLocalDataSource favLocal;
    private PlanLocalDataSource planLocal;
    private FirebaseFirestore db;
    private Context context;

    public AccountPresenterImp(AccountView view, Context context) {
        this.view = view;
        this.context = context;
        this.authService = new AuthService();
        this.sharedPref = new SharedPreferanceLocalDataSource(context);

        String userId = sharedPref.getUserId();
        this.favLocal = new FavoriteLocalDataSource(context, userId);
        this.planLocal = new PlanLocalDataSource(context, userId);
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onSyncClicked() {
        String userId = sharedPref.getUserId();

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
        favLocal.getFavoriteMeals()
                .take(1)
                .subscribe(favorites -> {
                    for (FavoriteEntity fav : favorites) {
                        db.collection("users").document(userId)
                                .collection("favorites").document(fav.idMeal).set(fav);
                    }
                }, throwable -> view.showSyncError(throwable.getMessage()));
    }

    private void uploadPlanFromLocal(String userId) {
        planLocal.getAllPlannedMeals()
                .take(1)
                .subscribe(plans -> {
                    for (PlanEntity plan : plans) {
                        db.collection("users").document(userId)
                                .collection("plan")
                                .document(plan.idMeal + "_" + plan.date).set(plan);
                    }
                    view.showSyncSuccess(); // نطلع رسالة النجاح في الآخر
                }, throwable -> view.showSyncError(throwable.getMessage()));
    }
    @Override
    public void onLogoutClicked() {
        String currentId = sharedPref.getUserId();
        favLocal.deleteAllFavorites()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> Log.d("LOGOUT", "Favorites deleted"),
                        throwable -> Log.e("LOGOUT", "Error deleting favorites"));

        planLocal.deleteAllPlans()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> Log.d("LOGOUT", "Plans deleted"),
                        throwable -> Log.e("LOGOUT", "Error deleting plans"));
        if ("GUEST".equals(currentId)) {
            sharedPref.clearUserData();
            view.navigateToLogin();
        } else {
            authService.logout();
            sharedPref.clearUserData();
            view.navigateToLogin();
        }
    }
}