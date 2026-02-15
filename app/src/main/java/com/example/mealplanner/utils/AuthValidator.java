package com.example.mealplanner.utils;

import android.util.Patterns;

public class AuthValidator {
    public static boolean isEmailValid(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isPasswordValid(String pass) {
        return !pass.isEmpty() && pass.length() >= 6;
    }
}
