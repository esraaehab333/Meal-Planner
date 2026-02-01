package com.example.mealplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        MaterialButton signInBtn = view.findViewById(R.id.signInTextView);
        signInBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_registerFregment_to_loginFregment);
        });
        return view;
    }
}