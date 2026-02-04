package com.example.mealplanner.data.network;

import com.example.mealplanner.datasource.auth.remote.AuthNetworkResponse;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
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
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void register(String email, String username,String password, AuthNetworkResponse callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    String uid = result.getUser().getUid();
                    saveUsernameInFirebaseStore(uid, username, email, callback);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void saveUsernameInFirebaseStore(String uid, String username, String email, AuthNetworkResponse callback){
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("uid", uid);
        firebaseStore.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure("Firestore Error: " + e.getMessage()));
    }

    public void loginWithGoogle(String idToken, AuthNetworkResponse callback){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(result->callback.onSuccess())
                .addOnFailureListener(e->callback.onFailure(e.getMessage()));
    }

    public void loginWithFacebook(String accessToken, AuthNetworkResponse callback){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(result->callback.onSuccess())
                .addOnFailureListener(e->callback.onFailure(e.getMessage()));
    }

    public void logout(){
        firebaseAuth.signOut();
    }

}
