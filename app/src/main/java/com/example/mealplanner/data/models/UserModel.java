package com.example.mealplanner.data.models;

public class UserModel {
    private String uid;
    private String username;
    private String email;

    // Required empty constructor for Firestore toObject()
    public UserModel() {}

    public UserModel(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}