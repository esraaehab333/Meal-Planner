package com.example.mealplanner.data.network;

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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthService {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseStore;

    public AuthService() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStore = FirebaseFirestore.getInstance();
    }

    public Single<String> login(String email, String password) {
        return Single.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                emitter.onSuccess(uid);
                            } else {
                                emitter.onError(new Exception(getErrorMessage(task.getException())));
                            }
                        }
                    });
        });
    }

    public Single<String> register(String email, String username, String password) {
        return Single.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                saveUsernameInFirebaseStore(uid, username, email)
                                        .subscribe(
                                                () -> emitter.onSuccess(uid),
                                                error -> emitter.onError(error)
                                        );
                            } else {
                                emitter.onError(new Exception(getErrorMessage(task.getException())));
                            }
                        }
                    });
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

    private Completable saveUsernameInFirebaseStore(String uid, String username, String email) {
        return Completable.create(emitter -> {
            Map<String, Object> user = new HashMap<>();
            user.put("username", username);
            user.put("email", email);
            user.put("uid", uid);

            firebaseStore.collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener(result -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(new Exception("Firestore Error: " + e.getMessage()));
                        }
                    });
        });
    }

    public Single<String> loginWithGoogle(String idToken) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                emitter.onSuccess(uid);
                            } else {
                                emitter.onError(new Exception(getErrorMessage(task.getException())));
                            }
                        }
                    });
        });
    }

    public Single<String> loginWithFacebook(String accessToken) {
        return Single.create(emitter -> {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                emitter.onSuccess(uid);
                            } else {
                                emitter.onError(new Exception(getErrorMessage(task.getException())));
                            }
                        }
                    });
        });
    }

    public void logout() {
        firebaseAuth.signOut();
    }
}