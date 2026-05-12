package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// Importing Firebase components for user registration
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // 1. Declaring UI variables
    EditText etSignUpEmail, etSignUpPassword;
    Button btnSignUpSubmit;
    TextView tvLoginNow;

    // Declaring the FirebaseAuth instance
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 2. Initializing Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Binding variables to XML views
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        btnSignUpSubmit = findViewById(R.id.btnSignUpSubmit);
        tvLoginNow = findViewById(R.id.tvLoginNow);

        // 4. Handling the registration logic when the button is clicked
        btnSignUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Extracting text from input fields
                String email = etSignUpEmail.getText().toString();
                String password = etSignUpPassword.getText().toString();

                // Simple validation to prevent app crash if fields are empty
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                    return; // Stop execution here
                }

                // Sending data to Firebase to create a new user profile
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If the account creation is successful
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                                    // Redirect the new user back to the login screen
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                // Display an error message if something fails (e.g. weak password)
                                else {
                                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // Navigate to the Login screen if the user clicks 'Login Now'
        tvLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}