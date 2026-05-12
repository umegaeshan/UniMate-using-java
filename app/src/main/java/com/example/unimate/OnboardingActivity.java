package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    ImageView btnBack, imgOnboarding;
    TextView tvSubtitle, tvDescription;
    Button btnContinue;

    int currentPage = 0; // Keeping track of the current slide index (0, 1, or 2)

    // Storing the titles for the 3 different onboarding slides in an array
    String[] subtitles = {
            "Create Your Tasks",
            "Organize Your Lists",
            "Track Your Progress"
    };

    // Storing the descriptions for the slides
    String[] descriptions = {
            "Add your daily tasks easily.\nWrite them down so you do not forget anything important.",
            "Group your tasks into folders.\nKeep your work and personal life separate and neat.",
            "See what you have finished.\nReach your goals and finish your day strong."
    };

    // Linking the illustration images from the drawable folder
    int[] images = {
            R.drawable.onboard_img_1,
            R.drawable.onboard_img_2,
            R.drawable.onboard_img_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        btnBack = findViewById(R.id.btnBack);
        imgOnboarding = findViewById(R.id.imgOnboarding);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvDescription = findViewById(R.id.tvDescription);
        btnContinue = findViewById(R.id.btnContinue);

        updateUI(); // Load the first slide immediately when the activity starts

        // Handling the logic to move to the next slide
        btnContinue.setOnClickListener(v -> {
            if (currentPage < 2) {
                currentPage++; // Move to the next page index
                updateUI();
            } else {
                // If it's the last slide, finish onboarding and move to the Login screen
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Handling the logic to navigate back through the slides
        btnBack.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--; // Move to the previous page
                updateUI();
            } else {
                finish(); // If we are on the first slide, pressing back closes the onboarding flow
            }
        });
    }

    private void updateUI() {
        tvSubtitle.setText(subtitles[currentPage]);
        tvDescription.setText(descriptions[currentPage]);

        try {
            imgOnboarding.setImageResource(images[currentPage]);
        } catch (Exception e) {
            imgOnboarding.setImageResource(R.mipmap.ic_launcher); // Fallback to app logo if image fails to load
        }

        // Change the button text to 'Get Started' on the final slide
        if (currentPage == 2) {
            btnContinue.setText("Get Started");
        } else {
            btnContinue.setText("Continue");
        }
    }
}