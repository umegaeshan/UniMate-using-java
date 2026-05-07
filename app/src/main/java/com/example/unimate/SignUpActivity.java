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

public class SignUpActivity extends AppCompatActivity {

    // 1. විචල්‍යයන් (නම්) හඳුන්වා දීම
    EditText etSignUpEmail, etSignUpPassword;
    Button btnSignUpSubmit;
    TextView tvLoginNow;

    // Firebase මුරකාරයා හඳුන්වා දීම
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 2. Firebase එකට සම්බන්ධ වීම
        mAuth = FirebaseAuth.getInstance();

        // 3. XML එකේ තියෙන දේවල් මේ නම් වලට සම්බන්ධ කිරීම
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        btnSignUpSubmit = findViewById(R.id.btnSignUpSubmit);
        tvLoginNow = findViewById(R.id.tvLoginNow);

        // 4. Sign Up බොත්තම එබුවම වෙන දේ
        btnSignUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // පෙට්ටි වල ගහපු Email එකයි Password එකයි ඇදලා අරන් නමකට දාගන්නවා
                String email = etSignUpEmail.getText().toString();
                String password = etSignUpPassword.getText().toString();

                // Email එක හරි Password එක හරි හිස් නම්, Error එකක් දෙනවා
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                    return; // මෙතනින් කේතය නවත්වනවා
                }

                // ඔක්කොම හරි නම්, Firebase එකට මේ විස්තර යවනවා ගිණුමක් හදන්න කියලා
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // වැඩේ සාර්ථකයි නම් (ළමයාව ලියාපදිංචි වුණා නම්)
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                                    // සාර්ථක නිසා ආපහු ලොග් වෙන පිටුවට (Login) යවනවා
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                // මොකක්හරි වැරදුණා නම් (උදා: Password එක කොට වැඩියි නම්)
                                else {
                                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // "Login Now" ලින්ක් එක එබුවම ආපහු Login පිටුවට යාම
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