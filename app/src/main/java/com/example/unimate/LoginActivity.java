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

// Firebase අදාළ දේවල් Import කිරීම
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnSignIn;
    TextView tvRegister;

    // Firebase මුරකාරයාව හඳුන්වා දීම
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase එකට සම්බන්ධ වීම
        mAuth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvRegister = findViewById(R.id.tvRegister);

        // Sign In බොත්තම එබුවම වෙන දේ
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // පෙට්ටි වලින් විස්තර ගන්නවා
                // (පොඩි සටහනක්: Firebase ලොග් වෙන්න අහන්නේ Email එකක් නිසා,
                // Username කියන පෙට්ටියෙත් අපි Email එක තමයි ටයිප් කරන්න ඕනේ)
                String email = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // පෙට්ටි හිස්ව තියලා එබුවොත් බනින්න
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ඔක්කොම හරි නම්, Firebase එකෙන් අහනවා මේක හරිද කියලා (signInWithEmailAndPassword)
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Password එකයි Email එකයි හරි නම්
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                    // ඊළඟට අපි ඇතුල් වෙන ප්‍රධාන පිටුව (Home Page) හැදුවාම,
                                    // මෙතනින් ඒ පිටුවට යන්න කේතය ලියනවා. දැනට මැසේජ් එකක් විතරක් එයි.
                                }
                                // මොකක්හරි වැරදුණා නම් (උදා: Password එක වැරදියි නම්)
                                else {
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // Register ලින්ක් එක එබුවම Sign Up පිටුවට යන කේතය (මේක කලින් තිබ්බ එකමයි)
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}