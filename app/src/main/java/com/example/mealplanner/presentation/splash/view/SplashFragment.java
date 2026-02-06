package com.example.mealplanner.presentation.splash.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mealplanner.R;

public class SplashFragment extends Fragment {
    LottieAnimationView lottieSplash;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        lottieSplash = view.findViewById(R.id.lottieSplash);
        lottieSplash.playAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (Navigation.findNavController(requireView()).getCurrentDestination().getId() == R.id.splashFragment) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_searchFragment);
            }
        }, 4500);
        return view;
    }

}