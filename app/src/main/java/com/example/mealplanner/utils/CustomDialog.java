package com.example.mealplanner.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealplanner.R;

public class CustomDialog extends DialogFragment {

    private int iconRes;
    private String title;
    private String message;
    private String positiveText;
    private String negativeText;
    private DialogInterface.OnClickListener positiveListener;
    private DialogInterface.OnClickListener negativeListener;

    public static CustomDialog newInstance(
            int iconRes,
            String title,
            String message,
            String positiveText,
            String negativeText,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener) {

        CustomDialog fragment = new CustomDialog();
        fragment.iconRes = iconRes;
        fragment.title = title;
        fragment.message = message;
        fragment.positiveText = positiveText;
        fragment.negativeText = negativeText;
        fragment.positiveListener = positiveListener;
        fragment.negativeListener = negativeListener;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);

        // Set icon
        ImageView dialogIcon = view.findViewById(R.id.dialog_icon);
        dialogIcon.setImageResource(iconRes);

        // Set title
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);

        // Set message
        TextView dialogMessage = view.findViewById(R.id.dialog_message);

        // Format message with HTML for colored text
        String formattedMessage = message;
        if (message.contains("<highlight>")) {
            formattedMessage = message.replace("<highlight>", "<font color='#FF6B4A'>")
                    .replace("</highlight>", "</font>");
            dialogMessage.setText(Html.fromHtml(formattedMessage, Html.FROM_HTML_MODE_LEGACY));
        } else {
            dialogMessage.setText(message);
        }

        // Set positive button
        Button btnPositive = view.findViewById(R.id.btn_positive);
        btnPositive.setText(positiveText);
        btnPositive.setOnClickListener(v -> {
            if (positiveListener != null) {
                positiveListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
            }
            dismiss();
        });

        // Set negative button
        Button btnNegative = view.findViewById(R.id.btn_negative);
        btnNegative.setText(negativeText);
        btnNegative.setOnClickListener(v -> {
            if (negativeListener != null) {
                negativeListener.onClick(getDialog(), DialogInterface.BUTTON_NEGATIVE);
            }
            dismiss();
        });

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }
}