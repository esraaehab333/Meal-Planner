package com.example.mealplanner.util;

import android.view.View;
import android.widget.TextView;

import com.example.mealplanner.R;
import com.google.android.material.snackbar.Snackbar;

public class CustomSnackbar {

    public static void showError(View view, String message) {
        show(view, message, R.color.snackbar_error);
    }

    public static void showSuccess(View view, String message) {
        show(view, message, R.color.snackbar_success);
    }

    private static void show(View view, String message, int bgColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        snackView.setBackgroundResource(R.drawable.shape_snack_bar_rounded);

        TextView text = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        text.setTextColor(view.getResources().getColor(android.R.color.white));
        text.setMaxLines(3);

        snackbar.show();
    }
}
