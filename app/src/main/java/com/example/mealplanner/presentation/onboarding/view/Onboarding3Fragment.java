package com.example.mealplanner.presentation.onboarding.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealplanner.R;
import com.google.android.material.button.MaterialButton;

public class Onboarding3Fragment extends Fragment {
    MaterialButton skipBtn , backBtn, nextBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding3, container, false);
        skipBtn = view.findViewById(R.id.btn_skip);
        backBtn = view.findViewById(R.id.backBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        skipBtn.setText("");
        skipBtn.setClickable(false);
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_onboarding3Fregment_to_onboarding2Fregment);
        });
        nextBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_onboarding3Fregment_to_loginFregment);
        });
        return view;
    }
}