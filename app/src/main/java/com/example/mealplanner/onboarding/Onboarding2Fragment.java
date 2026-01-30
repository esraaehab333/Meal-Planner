package com.example.mealplanner.onboarding;

import static android.view.View.GONE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealplanner.R;
import com.google.android.material.button.MaterialButton;

public class Onboarding2Fragment extends Fragment {
    MaterialButton skipBtn , backBtn, nextBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding2, container, false);
        skipBtn = view.findViewById(R.id.btn_skip);
        backBtn = view.findViewById(R.id.backBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        skipBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_onboarding2Fregment_to_loginFregment);
        });
        nextBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_onboarding2Fregment_to_onboarding3Fregment);
        });
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_onboarding2Fregment_to_onboarding1Fregment);
        });
        return view;
    }
}