package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

// Firebase මුරකාරයා ගේන්න
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Firebase එක ඇහැරවලා වැඩට ගන්නවා
        mAuth = FirebaseAuth.getInstance();

        // 2. මෙන්න මෙතැනදී තමයි අපි බලන්නේ කවුරුහරි කලින් ලොග් වෙලා ඉන්නවද කියලා
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // කෙනෙක් ලොග් වෙලා ඉන්නවා නම්, මේ පිටුව පෙන්වන්නේ නැතුව කෙලින්ම Task List එකට යන්න
            Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
            startActivity(intent);
            finish(); // මේ පිටුව වහලා දාන්න
            return; // ඉතිරි කේත වැඩ කරන්න ඕනේ නැහැ
        }

        // 3. කවුරුත් ලොග් වෙලා නැත්නම් විතරක් Start පිටුව පෙන්වන්න
        setContentView(R.layout.activity_main);

        // XML එකේ තියෙන බොත්තම කේතයට සම්බන්ධ කිරීම
        Button btnLetsStart = findViewById(R.id.btnLetsStart);

        // 4. Let's Start බොත්තම එබුවම Onboarding පිටුවට යාම පමණක් මෙහි තබා ඇත
        btnLetsStart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
            finish();
        });
    }
}