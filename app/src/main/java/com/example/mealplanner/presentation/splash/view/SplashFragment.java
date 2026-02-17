package com.example.mealplanner.presentation.splash.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mealplanner.R;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;

public class SplashFragment extends Fragment {
    LottieAnimationView lottieSplash;
    private AuthRepository authRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lottieSplash = view.findViewById(R.id.lottieSplash);
        authRepository = new AuthRepositoryImpl(requireActivity().getApplication());
        lottieSplash.playAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded() && getView() != null) {
                handleNavigation();
            }
        }, 4500);
    }


    private void handleNavigation() {
        if (isAdded() && Navigation.findNavController(requireView()).getCurrentDestination().getId() == R.id.splashFragment) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build();
            if (authRepository.getUserId() != null) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_homeFragment, null, navOptions);
            } else if (authRepository.isOnboardingCompleted()) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_loginFregment, null, navOptions);
            } else {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_onboarding1Fregment, null, navOptions);
            }
        }
    }
}