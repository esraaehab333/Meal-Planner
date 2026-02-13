package com.example.mealplanner.utils;

import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mealplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class CustomSnackbar {

    public static void showError(View view, String message) {
        show(view, message, R.color.color_secondary);
    }

    public static void showSuccess(View view, String message) {
        show(view, message, R.color.color_secondary);
    }

    private static void show(View view, String message, int colorRes) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View parent = view.getRootView();
        BottomNavigationView bottomNav = parent.findViewById(R.id.navigation);
        if (bottomNav != null) {
            snackbar.setAnchorView(bottomNav);
        }
        View snackView = snackbar.getView();
        snackView.setBackgroundResource(R.drawable.shape_snack_bar_rounded);
        snackView.setBackgroundTintList(ContextCompat.getColorStateList(view.getContext(), colorRes));
        TextView text = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        text.setTextColor(ContextCompat.getColor(view.getContext(), android.R.color.black));
        text.setMaxLines(3);
        snackbar.show();
    }
}