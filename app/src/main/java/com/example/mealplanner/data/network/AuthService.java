package com.example.mealplanner.data.network;

import com.example.mealplanner.datasource.auth.remote.AuthNetworkResponse;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseStore;

    public AuthService() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStore = FirebaseFirestore.getInstance();
    }
    public void login(String email, String password, AuthNetworkResponse callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(getErrorMessage(task.getException()));
                    }
                });
    }

    public void register(String email, String username, String password, AuthNetworkResponse callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();
                        saveUsernameInFirebaseStore(uid, username, email, callback);
                    } else {
                        callback.onFailure(getErrorMessage(task.getException()));
                    }
                });
    }

    private String getErrorMessage(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            return "No account found with this email.";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Incorrect email or password.";
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            return "This email is already registered.";
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return "The password is too weak.";
        } else if (e instanceof FirebaseNetworkException) {
            return "Network error. Check your connection.";
        } else {
            return e != null ? e.getLocalizedMessage() : "An unknown error occurred.";
        }
    }

    public void saveUsernameInFirebaseStore(String uid, String username, String email, AuthNetworkResponse callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("uid", uid);
        firebaseStore.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure("Firestore Error: " + e.getMessage()));
    }

    public void loginWithGoogle(String idToken, AuthNetworkResponse callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(getErrorMessage(task.getException()));
                });
    }

    public void loginWithFacebook(String accessToken, AuthNetworkResponse callback) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(getErrorMessage(task.getException()));
                });
    }

    public void logout() {
        firebaseAuth.signOut();
    }
}