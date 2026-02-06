package com.example.mealplanner.presentation.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.presentation.auth.presenter.LoginPresenterImp;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class LoginFragment extends Fragment implements AuthView {
    private TextInputEditText emailTextInput;
    private TextInputEditText passwordTextInput;
    private AppCompatButton signInBtn;
    private MaterialButton signUpBtn;
    private MaterialButton googleBtn, facebookBtn;
    private LoginPresenterImp presenter;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.facebook.FacebookSdk.sdkInitialize(requireContext().getApplicationContext());
        setupGoogleSignIn();
        callbackManager = CallbackManager.Factory.create();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleGoogleSignInResult(task);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        presenter = new LoginPresenterImp(this);
        signInBtn.setOnClickListener(v -> {
            String email = emailTextInput.getText().toString().trim();
            String pass = passwordTextInput.getText().toString().trim();
            if (!email.isEmpty() && !pass.isEmpty()) {
                presenter.login(email, pass);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        signUpBtn.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFregment_to_registerFregment)
        );
        googleBtn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
        facebookBtn.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    presenter.loginWithFacebook(loginResult.getAccessToken().getToken());
                }
                @Override
                public void onCancel() {
                    Toast.makeText(getContext(), "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(@NonNull FacebookException error) {
                }
            });
        });

        return view;
    }
    private void initViews(View view) {
        emailTextInput = view.findViewById(R.id.emailEditText);
        passwordTextInput = view.findViewById(R.id.passwordEditText);
        signInBtn = view.findViewById(R.id.loginButton);
        signUpBtn = view.findViewById(R.id.signUpTextView);
        googleBtn = view.findViewById(R.id.googleButton);
        facebookBtn = view.findViewById(R.id.facebookButton);
    }
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                presenter.loginWithGoogle(account.getIdToken());
            }
        } catch (ApiException e) {
            onError("Google Sign-In failed: " + e.getStatusCode());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onSuccess(String message) {
        if (isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFregment_to_homeFragment);
        }
    }
    @Override
    public void onError(String errorMessage) {
        if (isAdded()) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void showLoading() { }
    @Override
    public void hideLoading() { }
}