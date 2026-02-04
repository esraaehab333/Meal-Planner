package com.example.mealplanner.presentation.auth.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.data.network.AuthService;
import com.example.mealplanner.presentation.auth.presenter.LoginPresenterImp;
import com.example.mealplanner.presentation.auth.presenter.RegisterPresenterImp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements AuthView{
    private FirebaseAuth firebaseAuth;
    AuthService authService;
    LoginPresenterImp presenter;
    private TextInputEditText emailTextInput;
    private TextInputEditText passwordTextInput;
    private AppCompatButton signInBtn;
    private MaterialButton signUpBtn;
    private MaterialButton googleBtn , facebookBtn;
    private AppCompatButton continueAsGustBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        signUpBtn = view.findViewById(R.id.signUpTextView);
        signInBtn = view.findViewById(R.id.loginButton);
        passwordTextInput = view.findViewById(R.id.passwordEditText);
        emailTextInput = view.findViewById(R.id.emailEditText);
        presenter = new LoginPresenterImp( this);
        signUpBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFregment_to_registerFregment);
        });
        signInBtn.setOnClickListener(v -> {
            String email = emailTextInput.getText().toString().trim();
            String pass = passwordTextInput.getText().toString().trim();
            presenter.login(email,pass);
        });
        return view;
    }

    @Override
    public void showLoading() {}
    @Override
    public void hideLoading() {}

    @Override
    public void onSuccess(String message) {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFregment_to_homeFragment);
        Toast.makeText(getContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(String errorMessage) {
        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
    }
}