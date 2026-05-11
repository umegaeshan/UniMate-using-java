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

    int currentPage = 0; // දැනට ඉන්න පිටුව (0, 1, හෝ 2)

    // පිටු 3ට අදාළ විස්තර Arrays වලට දාලා තියෙනවා
    String[] subtitles = {
            "Create Your Tasks",
            "Organize Your Lists",
            "Track Your Progress"
    };

    String[] descriptions = {
            "Add your daily tasks easily.\nWrite them down so you do not forget anything important.",
            "Group your tasks into folders.\nKeep your work and personal life separate and neat.",
            "See what you have finished.\nReach your goals and finish your day strong."
    };

    // ඔයා drawable ෆෝල්ඩරයට දාපු පින්තූර 3 මේකට ඇතුළත් කරන්න
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

        updateUI(); // මුලින්ම ඇප් එක ඕපන් වෙද්දී පළමු පිටුව ලෝඩ් කිරීම

        // Continue බොත්තම එබූ විට
        btnContinue.setOnClickListener(v -> {
            if (currentPage < 2) {
                currentPage++; // ඊළඟ පිටුවට යනවා
                updateUI();
            } else {
                // පිටු 3 ම ඉවර නම් Login එකට හෝ Main එකට යනවා
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Back (<) බොත්තම එබූ විට
        btnBack.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--; // කලින් පිටුවට යනවා
                updateUI();
            } else {
                finish(); // පළවෙනි පිටුවේ ඉඳන් back කළොත් Splash එකට යනවා
            }
        });
    }

    private void updateUI() {
        tvSubtitle.setText(subtitles[currentPage]);
        tvDescription.setText(descriptions[currentPage]);

        try {
            imgOnboarding.setImageResource(images[currentPage]);
        } catch (Exception e) {
            imgOnboarding.setImageResource(R.mipmap.ic_launcher); // පින්තූරය නැත්නම් ලෝගෝ එක පෙන්වයි
        }

        // අන්තිම පිටුවේදී බොත්තමේ නම වෙනස් කිරීම
        if (currentPage == 2) {
            btnContinue.setText("Get Started");
        } else {
            btnContinue.setText("Continue");
        }
    }
}