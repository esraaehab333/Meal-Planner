package com.example.mealplanner.datasource.auth.remote;

import com.example.mealplanner.data.models.UserModel;
import com.example.mealplanner.data.network.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSource {
    private AuthService authService;

    public AuthRemoteDataSource() {
        authService = new AuthService();
    }
    public Single<UserModel> login(String email, String password) {
        return authService.login(email, password)
                .flatMap(this::getUserFromFirestore);
    }
    public Single<UserModel> register(String email, String username, String password) {
        return authService.register(email, username, password)
                .map(uid -> new UserModel(uid, username, email));
    }
    public Single<UserModel> loginWithGoogle(String idToken) {
        return authService.loginWithGoogle(idToken)
                .flatMap(uid -> getUserFromFirestore(uid)
                        .onErrorResumeNext(throwable -> {
                            String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            return authService.saveUsernameInFirebaseStore(uid, name, email)
                                    .andThen(Single.just(new UserModel(uid, name, email)));
                        }));
    }

    public Single<UserModel> loginWithFacebook(String accessToken) {
        return authService.loginWithFacebook(accessToken)
                .flatMap(uid -> getUserFromFirestore(uid)
                        .onErrorResumeNext(throwable -> {
                            String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            return authService.saveUsernameInFirebaseStore(uid, name, email)
                                    .andThen(Single.just(new UserModel(uid, name, email)));
                        }));
    }

    public void logout() {
        authService.logout();
    }

    public Single<UserModel> getUserFromFirestore(String uid) {
        return Single.create(emitter -> {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            UserModel user = documentSnapshot.toObject(UserModel.class);
                            if (user != null) {
                                emitter.onSuccess(user);
                            } else {
                                emitter.onError(new Exception("Failed to parse user data"));
                            }
                        } else {
                            emitter.onError(new Exception("User profile not found in database"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}