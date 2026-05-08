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

        if (user != null) {
            etEmail.setText(user.getEmail());
            etPassword.setText("........");
            db.collection("users").document(user.getUid())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("username");
                            tvProfileName.setText(name != null ? name : "User");
                            currentMobile = documentSnapshot.getString("mobile");
                            if (currentMobile != null && !currentMobile.trim().isEmpty()) {
                                etMobile.setText(maskMobileNumber(currentMobile));
                            }
                        }
                    });
        }

        btnUpdate.setOnClickListener(v -> {
            if (!isEditing) {
                isEditing = true;
                btnUpdate.setText("Save");
                btnLogout.setText("Cancel");
                etMobile.setEnabled(true);
                etPassword.setEnabled(true);
                etPassword.setText("");
                etMobile.setText(currentMobile);
            } else {
                if (user != null) {
                    String newMobile = etMobile.getText().toString();
                    String newPassword = etPassword.getText().toString();
                    if (!newPassword.isEmpty()) {
                        user.updatePassword(newPassword);
                    }
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("mobile", newMobile);
                    db.collection("users").document(user.getUid())
                            .set(userData, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                currentMobile = newMobile;
                                lockProfileFields();
                                Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        btnLogout.setOnClickListener(v -> {
            if (isEditing) {
                lockProfileFields();
            } else {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Navigation Bar Fix
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, TaskListActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_completed) {
                startActivity(new Intent(ProfileActivity.this, CompletedTasksActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_dev) {
                startActivity(new Intent(ProfileActivity.this, DevInfoActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_profile;
        });
    }

    private void lockProfileFields() {
        isEditing = false;
        btnUpdate.setText("Update");
        btnLogout.setText("Log Out");
        etMobile.setEnabled(false);
        etPassword.setEnabled(false);
        etPassword.setText("........");
        if(currentMobile != null && !currentMobile.trim().isEmpty()) {
            etMobile.setText(maskMobileNumber(currentMobile));
        }
    }

    private String maskMobileNumber(String mobile) {
        if (mobile == null || mobile.length() <= 6) return mobile;
        return mobile.substring(0, 3) + "***" + mobile.substring(mobile.length() - 3);
    }
}