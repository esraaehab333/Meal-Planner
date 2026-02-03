package com.example.mealplanner.presentation.splash.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealplanner.R;

public class SplashFragment extends Fragment {

    ImageView logo;
    TextView appName, subTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        logo = view.findViewById(R.id.imageView2);
        appName = view.findViewById(R.id.app_name_txtView);
        subTitle = view.findViewById(R.id.subtitle_app_txtView);

        Animation logoAnim = AnimationUtils.loadAnimation(getContext(), R.anim.logo_animation);
        logo.startAnimation(logoAnim);

        appName.setAlpha(0f);
        subTitle.setAlpha(0f);
        appName.animate().alpha(1f).setDuration(800).setStartDelay(600).start();
        subTitle.animate().alpha(1f).setDuration(800).setStartDelay(1000).start();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ Use Handler to delay navigation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (NavHostFragment.findNavController(this).getCurrentDestination() != null &&
                    NavHostFragment.findNavController(this).getCurrentDestination().getId() == R.id.splashFragment) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_splashFragment_to_onboarding1Fregment);
            }
        }, 2500);
    }
}
