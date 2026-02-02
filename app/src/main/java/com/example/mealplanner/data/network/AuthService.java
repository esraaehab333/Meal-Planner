package com.example.mealplanner.data.network;

import com.example.mealplanner.datasource.auth.remote.AuthNetworkResponse;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {

    private FirebaseAuth firebaseAuth;

    public AuthService() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, AuthNetworkResponse callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void register(String email, String password, AuthNetworkResponse callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
