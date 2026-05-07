package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText etEmail, etPassword, etMobile;
    TextView tvProfileName;
    Button btnLogout, btnUpdate;

    boolean isEditing = false;

    // දත්ත Cancel කරද්දී ආපහු දාන්න මතක තියාගන්න Variables
    String currentMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etMobile = findViewById(R.id.etMobile);
        tvProfileName = findViewById(R.id.tvProfileName);
        btnLogout = findViewById(R.id.btnLogout);
        btnUpdate = findViewById(R.id.btnUpdate);

        // මුලින්ම Database එකෙන් විස්තර අරගෙන පෙට්ටි වලට දානවා
        if (user != null) {
            etEmail.setText(user.getEmail());
            etPassword.setText("........");

            db.collection("users").document(user.getUid())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            // නම අරගෙන Text එකට දානවා
                            String name = documentSnapshot.getString("username");
                            if (name != null) {
                                tvProfileName.setText(name);
                            } else {
                                tvProfileName.setText("User");
                            }

                            // ෆෝන් නම්බර් එක අරගෙන පෙට්ටියට දානවා
                            currentMobile = documentSnapshot.getString("mobile");
                            if (currentMobile != null && !currentMobile.trim().isEmpty()) {
                                etMobile.setText(maskMobileNumber(currentMobile));
                            } else {
                                currentMobile = "";
                                etMobile.setText(""); // නම්බර් එකක් නැත්නම් හිස්ව තියෙනවා
                            }
                        }
                    });
        }

        // ==============================================
        // Update / Save බොත්තම
        // ==============================================
        btnUpdate.setOnClickListener(v -> {
            if (!isEditing) {
                // UPDATE එබුවම
                isEditing = true;
                btnUpdate.setText("Save");
                btnLogout.setText("Cancel");

                etMobile.setEnabled(true);
                etPassword.setEnabled(true);

                etPassword.setText(""); // අලුත් එකක් ගහන්න හිස් කරනවා
                etMobile.setText(currentMobile); // Edit කරන්න ලේසි වෙන්න ඇත්ත අංකය පෙන්නනවා

            } else {
                // SAVE එබුවම
                if (user != null) {
                    String newMobile = etMobile.getText().toString();
                    String newPassword = etPassword.getText().toString();

                    if (!newPassword.isEmpty()) {
                        user.updatePassword(newPassword).addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("mobile", newMobile);

                    db.collection("users").document(user.getUid())
                            .set(userData, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {

                                currentMobile = newMobile; // අලුත් එක මතක තියාගන්නවා
                                lockProfileFields();
                                Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        // ==============================================
        // Log Out / Cancel බොත්තම
        // ==============================================
        btnLogout.setOnClickListener(v -> {
            if (isEditing) {
                // CANCEL එබුවම
                etMobile.setText(currentMobile); // පරණ දත්ත ආපහු දානවා
                lockProfileFields(); // ආපහු Lock කරනවා

            } else {
                // LOG OUT එබුවම
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // ==============================================
        // Navigation Bar
        // ==============================================
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_completed) {
                Toast.makeText(this, "Completed Soon", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_dev) {
                startActivity(new Intent(getApplicationContext(), DevInfoActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    // පෙට්ටි ලොක් කරන ක්‍රමය
    private void lockProfileFields() {
        isEditing = false;
        btnUpdate.setText("Update");
        btnLogout.setText("Log Out");

        etMobile.setEnabled(false);
        etPassword.setEnabled(false);

        etPassword.setText("........");

        // සේව්/කැන්සල් කළාට පස්සේ ආපහු තරු දාලා පෙන්වනවා
        if(currentMobile != null && !currentMobile.trim().isEmpty()) {
            etMobile.setText(maskMobileNumber(currentMobile));
        } else {
            etMobile.setText("");
        }
    }

    // ෆෝන් නම්බර් එක මැදට තරු (***) දාන ක්‍රමය
    private String maskMobileNumber(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return "";
        }
        mobile = mobile.trim();
        if (mobile.length() <= 6) {
            return mobile;
        }

        String firstThree = mobile.substring(0, 3);
        String lastThree = mobile.substring(mobile.length() - 3);
        return firstThree + "***" + lastThree;
    }
}