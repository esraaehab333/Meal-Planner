package com.example.mealplanner.utils;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealplanner.R;

public class CustomDialog {

    public static void showGuestDialog(Fragment fragment) {
        new AlertDialog.Builder(fragment.requireContext())
                .setTitle("Guest Mode")
                .setMessage("You are browsing as a guest. To save your favorite meals permanently, please create an account.")
                .setPositiveButton("Register Now", (dialog, which) -> {
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_favoritesFragment_to_registerFregment);
                })
                .setNegativeButton("Stay as Guest", null)
                .show();
    }
}
