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
import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;

public class SplashFragment extends Fragment {
    LottieAnimationView lottieSplash;
    private SharedPreferanceDao sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lottieSplash = view.findViewById(R.id.lottieSplash);
        sharedPref = new SharedPreferanceLocalDataSource(requireContext());
        lottieSplash.playAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded() && getView() != null) {
                handleNavigation();
            }
        }, 4500);
    }


    private void handleNavigation() {
        // التحقق إننا لسه واقفين في الـ Splash عشان ميحصلش Crash لو الـ Handler اتأخر
        if (isAdded() && Navigation.findNavController(requireView()).getCurrentDestination().getId() == R.id.splashFragment) {

            // تعريف الـ NavOptions لمسح الـ SplashFragment من الـ Back Stack تماماً
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build();

            if (sharedPref.getUserId() != null) {
                // مستخدم مسجل أو Guest -> للـ Home ومسح الـ Splash
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_homeFragment, null, navOptions);
            } else if (sharedPref.isOnboardingCompleted()) {
                // خلص الأونبوردينج بس مش مسجل -> للـ Login ومسح الـ Splash
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_loginFregment, null, navOptions);
            } else {
                // مستخدم جديد أول مرة -> للأونبوردينج ومسح الـ Splash
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_onboarding1Fregment, null, navOptions);
            }
        }
    }
}