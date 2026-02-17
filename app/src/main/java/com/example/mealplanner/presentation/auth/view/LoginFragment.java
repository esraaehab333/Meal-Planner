package com.example.mealplanner.presentation.auth.view;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.presentation.auth.presenter.LoginPresenterImp;
import com.example.mealplanner.utils.CustomDialog;
import com.example.mealplanner.utils.CustomSnackbar;
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
    private AppCompatButton asGustBtn;
    private ProgressBar loginProgressBar;

    private LoginPresenterImp presenter;
    private SharedPreferanceDao sharedPref;
    private GoogleSignInClient googleClient;
    private ActivityResultLauncher<Intent> googleLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGoogle();
    }

    private void setupGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleClient = GoogleSignIn.getClient(requireActivity(), gso);

        googleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                        try {
                            GoogleSignInAccount acc = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                    .getResult(ApiException.class);
                            presenter.loginWithGoogle(acc.getIdToken());
                        } catch (ApiException e) {
                            CustomSnackbar.showError(requireView(), "Google login failed");
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initViews(view);

        sharedPref = new SharedPreferanceLocalDataSource(getContext());
        presenter = new LoginPresenterImp(requireActivity().getApplication(),this);

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

        asGustBtn.setOnClickListener(v -> {
            String message = "You are about to continue as a Guest. Some features may be limited.";
            CustomDialog dialog = CustomDialog.newInstance(
                    R.drawable.ic_guest,
                    "Continue as Guest?",
                    message,
                    "Continue",
                    "Cancel",
                    (dialogInterface, which) -> {
                        presenter.loginAsGuest();
                    },
                    null
            );
            dialog.show(getParentFragmentManager(), "GuestDialog");
        });

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
        asGustBtn = view.findViewById(R.id.asGustButton);
        loginProgressBar = view.findViewById(R.id.loginProgressBar);
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (!AuthValidator.isEmailValid(emailET.getText().toString().trim())) {
            emailLayout.setError("Invalid email");
            valid = false;
        } else {
            emailLayout.setError(null);
        }

        if (!AuthValidator.isPasswordValid(passwordET.getText().toString().trim())) {
            passwordLayout.setError("Invalid password");
            valid = false;
        } else {
            passwordLayout.setError(null);
        }

        return valid;
    }

    @Override
    public void onSuccess(String message) {
        CustomSnackbar.showSuccess(requireView(), message);
        NavOptions loginNavOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFregment, true)
                .build();
        Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFregment_to_homeFragment, null, loginNavOptions);
    }

    @Override
    public void onError(String message) {
        hideLoading();
        CustomSnackbar.showError(requireView(), message);
    }

    @Override
    public void showLoading() {
        loginBtn.setEnabled(false);
        loginBtn.setText("");  // Hide text
        loginProgressBar.setVisibility(View.VISIBLE);

        // Disable other interactive elements
        emailET.setEnabled(false);
        passwordET.setEnabled(false);
        googleBtn.setEnabled(false);
        asGustBtn.setEnabled(false);
        signUpBtn.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        loginBtn.setEnabled(true);
        loginBtn.setText(R.string.sign_in);  // Restore text
        loginProgressBar.setVisibility(View.GONE);

        // Re-enable other interactive elements
        emailET.setEnabled(true);
        passwordET.setEnabled(true);
        googleBtn.setEnabled(true);
        asGustBtn.setEnabled(true);
        signUpBtn.setEnabled(true);
    }
}