package com.example.mealplanner;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private View bottomShadow;
    private SharedPreferanceDao sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FirebaseApp.initializeApp(this);
        } catch (IllegalStateException e) { }

        setContentView(R.layout.activity_main);

        sharedPref = new SharedPreferanceLocalDataSource(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomShadow = findViewById(R.id.bottom_shadow);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            handleStartDestination(navController);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                boolean isMainScreen = id == R.id.homeFragment ||
                        id == R.id.plannerFragment ||
                        id == R.id.favoritesFragment ||
                        id == R.id.accountFragment;

                showBottomNavigation(isMainScreen);
            });
        }
    }
    private void handleStartDestination(NavController navController) {
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav);
        navGraph.setStartDestination(R.id.splashFragment);
        navController.setGraph(navGraph);
    }

    private void showBottomNavigation(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        bottomNavigationView.setVisibility(visibility);
        if (bottomShadow != null) {
            bottomShadow.setVisibility(visibility);
        }
    }
}