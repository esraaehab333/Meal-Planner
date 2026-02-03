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
import com.example.mealplanner.presentation.auth.presenter.RegisterPresenterImp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment implements AuthView{

    private FirebaseAuth firebaseAuth;
    AuthService authService;
    RegisterPresenterImp presenter;
    private TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText, usernameEditText;
    private AppCompatButton signUpButton;
    private MaterialButton signInBtn;
    private MaterialButton googleBtn , facebookBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        signUpButton = view.findViewById(R.id.signUpButton);
        signInBtn = view.findViewById(R.id.signInTextView);
        presenter = new RegisterPresenterImp( this);
        signInBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFregment_to_loginFregment);
        });
        signUpButton.setOnClickListener(v -> {
            String name = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String pass = passwordEditText.getText().toString().trim();
            presenter.register(email, name, pass);
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
                .navigate(R.id.action_registerFregment_to_loginFregment);
        Toast.makeText(getContext(), "Success Registration!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(String errorMessage) {
        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
    }
}