package com.example.mealplanner.presentation.auth.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealplanner.R;
import com.google.android.material.button.MaterialButton;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        MaterialButton signUpBtn = view.findViewById(R.id.signUpTextView);
        signUpBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_loginFregment_to_registerFregment);
        });
        return view;
    }
}