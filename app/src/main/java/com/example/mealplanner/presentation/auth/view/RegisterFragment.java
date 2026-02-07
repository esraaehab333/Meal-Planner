package com.example.mealplanner.presentation.auth.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.presentation.auth.presenter.RegisterPresenterImp;
import com.example.mealplanner.utils.CustomSnackbar;
import com.example.mealplanner.utils.AuthValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment implements AuthView {

    private RegisterPresenterImp presenter;

    private TextInputEditText usernameET, emailET, passwordET, confirmPasswordET;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout, confirmPasswordLayout;

    private AppCompatButton signUpBtn;
    private MaterialButton signInBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);
        presenter = new RegisterPresenterImp(this);

        signInBtn.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_registerFregment_to_loginFregment)
        );

        signUpBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                presenter.register(
                        emailET.getText().toString().trim(),
                        usernameET.getText().toString().trim(),
                        passwordET.getText().toString().trim()
                );
            }
        });

        return view;
    }

    private void initViews(View view) {
        usernameET = view.findViewById(R.id.usernameEditText);
        emailET = view.findViewById(R.id.emailEditText);
        passwordET = view.findViewById(R.id.passwordEditText);
        confirmPasswordET = view.findViewById(R.id.confirmPasswordEditText);

        usernameLayout = view.findViewById(R.id.usernameTextInputLayout);
        emailLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordLayout = view.findViewById(R.id.passwordTextInputLayout);
        confirmPasswordLayout = view.findViewById(R.id.confirmPasswordTextInputLayout);

        signUpBtn = view.findViewById(R.id.signUpButton);
        signInBtn = view.findViewById(R.id.signInTextView);
    }

    private boolean validateInputs() {
        boolean valid = true;

        String name = usernameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String pass = passwordET.getText().toString().trim();
        String confirm = confirmPasswordET.getText().toString().trim();

        usernameLayout.setError(name.isEmpty() ? "Username required" : null);

        if (!AuthValidator.isEmailValid(email)) {
            emailLayout.setError("Invalid email");
            valid = false;
        } else emailLayout.setError(null);

        if (!AuthValidator.isPasswordValid(pass)) {
            passwordLayout.setError("Password must be at least 6 chars");
            valid = false;
        } else passwordLayout.setError(null);

        if (!pass.equals(confirm)) {
            confirmPasswordLayout.setError("Passwords do not match");
            valid = false;
        } else confirmPasswordLayout.setError(null);

        return valid && !name.isEmpty();
    }

    @Override
    public void onSuccess(String message) {
        CustomSnackbar.showSuccess(requireView(), "Account Created Successfully");
        Navigation.findNavController(requireView())
                .navigate(R.id.action_registerFregment_to_loginFregment);
    }

    @Override
    public void onError(String message) {
        hideLoading();
        CustomSnackbar.showError(requireView(), message);
    }

    @Override
    public void showLoading() {
        signUpBtn.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        signUpBtn.setEnabled(true);
    }

    @Override public void setEmailError(String error) {
        emailLayout.setError(error);
    }
    @Override public void setPasswordError(String error) {
        passwordLayout.setError(error);
    }
}
