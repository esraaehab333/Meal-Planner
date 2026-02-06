package com.example.mealplanner.presentation.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.presentation.auth.presenter.LoginPresenterImp;
import com.example.mealplanner.util.CustomSnackbar;
import com.example.mealplanner.utils.AuthValidator;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements AuthView {

    private TextInputEditText emailET, passwordET;
    private TextInputLayout emailLayout, passwordLayout;
    private AppCompatButton loginBtn;
    private MaterialButton signUpBtn, googleBtn;

    private LoginPresenterImp presenter;
    private GoogleSignInClient googleClient;
    private ActivityResultLauncher<Intent> googleLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGoogle();
    }

    private void setupGoogle() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build();

        googleClient = GoogleSignIn.getClient(requireActivity(), gso);

        googleLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                                try {
                                    GoogleSignInAccount acc =
                                            GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                                    .getResult(ApiException.class);
                                    presenter.loginWithGoogle(acc.getIdToken());
                                } catch (ApiException e) {
                                    CustomSnackbar.showError(requireView(), "Google login failed");
                                }
                            }
                        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        presenter = new LoginPresenterImp(this);

        loginBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                presenter.login(
                        emailET.getText().toString().trim(),
                        passwordET.getText().toString().trim()
                );
            }
        });

        signUpBtn.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_loginFregment_to_registerFregment)
        );

        googleBtn.setOnClickListener(v ->
                googleLauncher.launch(googleClient.getSignInIntent())
        );

        return view;
    }

    private void initViews(View view) {
        emailET = view.findViewById(R.id.emailEditText);
        passwordET = view.findViewById(R.id.passwordEditText);
        emailLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordLayout = view.findViewById(R.id.passwordTextInputLayout);
        loginBtn = view.findViewById(R.id.loginButton);
        signUpBtn = view.findViewById(R.id.signUpTextView);
        googleBtn = view.findViewById(R.id.googleButton);
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (!AuthValidator.isEmailValid(emailET.getText().toString().trim())) {
            emailLayout.setError("Invalid email");
            valid = false;
        } else emailLayout.setError(null);

        if (!AuthValidator.isPasswordValid(passwordET.getText().toString().trim())) {
            passwordLayout.setError("Invalid password");
            valid = false;
        } else passwordLayout.setError(null);

        return valid;
    }

    @Override
    public void onSuccess(String message) {
        CustomSnackbar.showSuccess(requireView(), message);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFregment_to_homeFragment);
    }

    @Override
    public void onError(String message) {
        hideLoading();
        CustomSnackbar.showError(requireView(), message);
    }

    @Override public void showLoading() {
        loginBtn.setEnabled(false);
    }

    @Override public void hideLoading() {
        loginBtn.setEnabled(true);
    }

    @Override public void setEmailError(String error) {
        emailLayout.setError(error);
    }

    @Override public void setPasswordError(String error) {
        passwordLayout.setError(error);
    }
}
