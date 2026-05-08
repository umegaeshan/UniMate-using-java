package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DevInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // අර අපි Error එනවා කියලා අයින් කරපු කෑල්ල මතකද? මෙතනත් ඒක නැතුව පිරිසිදුවට තියෙනවා.
        setContentView(R.layout.activity_dev_info);

        // 1. Navigation Bar එක අල්ලගැනීම
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // මේ පිටුවට ආවම 'Dev Info' කියන අයිකන් එක තේරිලා තියෙන්න ඕනේ
        bottomNavigationView.setSelectedItemId(R.id.nav_dev);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Home එබුවම ආපහු Task List එකට යනවා
                startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                overridePendingTransition(0, 0); // ඇනිමේෂන් එකක් නැතුව සුමටව මාරු වෙන්න
                finish();
                return true;
            } else if (id == R.id.nav_completed) {
                Toast.makeText(DevInfoActivity.this, "Completed Tasks Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_dev) {
                return true; // දැනටමත් ඉන්නේ මෙතන නිසා මුකුත් කරන්නේ නැහැ
            } else if (id == R.id.nav_profile) {
                Toast.makeText(DevInfoActivity.this, "Profile Page Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_completed) {
                startActivity(new Intent(getApplicationContext(), CompletedTasksActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        // 2. Exit බොත්තමට පණ දීම
        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // මේකෙන් කරන්නේ ඇප් එකෙන් සම්පූර්ණයෙන්ම එළියට යන එකයි (Close App)
                finishAffinity();
                System.exit(0);
            }
        });
    }
}