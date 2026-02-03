package com.example.mealplanner;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mealplanner.presentation.splash.view.SplashFragment;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase BEFORE anything else
        try {
            FirebaseApp.initializeApp(this);
        } catch (IllegalStateException e) {
            // Already initialized
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {  // ← Prevent duplicate fragments on rotation
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.splashFragment, new SplashFragment())
                    .commit();
        }
    }
}