package com.example.mealplanner;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private View bottomShadow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FirebaseApp.initializeApp(this);
        } catch (IllegalStateException e) {
        }

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomShadow = findViewById(R.id.bottom_shadow);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                boolean isMainScreen = id == R.id.homeFragment ||
                        id == R.id.plannerFragment ||
                        id == R.id.favoritesFragment ||
                        id == R.id.accountFragment;
                if (isMainScreen) {
                    showBottomNavigation(true);
                } else {
                    showBottomNavigation(false);
                }
            });
        }
    }
    private void showBottomNavigation(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        bottomNavigationView.setVisibility(visibility);
        if (bottomShadow != null) {
            bottomShadow.setVisibility(visibility);
        }
    }
}