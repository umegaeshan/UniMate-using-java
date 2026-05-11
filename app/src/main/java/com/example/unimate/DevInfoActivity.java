package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DevInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        // Bottom Navigation Bar එකට පණ දීම
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_dev);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(DevInfoActivity.this, TaskListActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_completed) {
                startActivity(new Intent(DevInfoActivity.this, CompletedTasksActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_dev) {
                return true; // දැනට ඉන්නේ මේ පිටුවේ
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(DevInfoActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            }
            return false;
        });

        // Top Bar Icons (Settings & Notification)
        ImageView btnSettings = findViewById(R.id.btnSettings);
        ImageView btnNotification = findViewById(R.id.btnNotification);

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Settings");
                int currentMode = AppCompatDelegate.getDefaultNightMode();
                boolean isDarkMode = (currentMode == AppCompatDelegate.MODE_NIGHT_YES);
                String[] options = {isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode"};

                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
                    }
                });
                builder.show();
            });
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        // btnExit එකට අදාළ කේතය සම්පූර්ණයෙන්ම ඉවත් කර ඇත
    }
}