package com.example.mealplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        logo.startAnimation(logoAnim);

        appName.setAlpha(0f);
        subTitle.setAlpha(0f);

        appName.animate().alpha(1f).setDuration(800).setStartDelay(600).start();
        subTitle.animate().alpha(1f).setDuration(800).setStartDelay(1000).start();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_splashFragment_to_placeholder);
        }, 2500);

        return view;
    }
}
