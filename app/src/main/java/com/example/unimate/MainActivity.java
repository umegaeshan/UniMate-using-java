package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

// Importing Firebase to check login states
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Checking if a user is currently logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // If the user is already authenticated, bypass this welcome screen and load the main app
            Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
            startActivity(intent);
            finish(); // Closing this activity so the user can't go back to it
            return; // Exiting the method so the rest of the code doesn't execute
        }

        // If no user is logged in, we show the welcome layout
        setContentView(R.layout.activity_main);

        // Binding the start button from the XML
        Button btnLetsStart = findViewById(R.id.btnLetsStart);

        // Directing new users to the onboarding screens to explain app features
        btnLetsStart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
            finish();
        });
    }
}