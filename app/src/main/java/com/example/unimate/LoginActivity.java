package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // 1. අපි XML එකේ හදපු දේවල් වලට මෙතන නම් දාගන්නවා
    EditText etUsername, etPassword;
    Button btnSignIn;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 2. XML එකේ තියෙන දේවල් මේ නම් වලට සම්බන්ධ කරනවා (ID එකෙන්)
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvRegister = findViewById(R.id.tvRegister);

        // 3. Sign In බොත්තම එබුවම වෙන දේ ලියනවා
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // පරිශීලකයා ගහපු Username එකයි Password එකයි අරගන්නවා
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // දැනට තිරයේ පොඩි මැසේජ් එකක් (Toast) පෙන්නනවා
                Toast.makeText(LoginActivity.this, "Signing in...", Toast.LENGTH_SHORT).show();

                // (පස්සේ අපි මේ හරියට Firebase එකෙන් ඇත්තටම ලොග් වෙන කේතය දානවා)
            }
        });

        // 4. Register වෙන ලින්ක් එක එබුවම වෙන දේ
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // (අපි තාම Sign Up පිටුව හැදුවෙ නැති නිසා දැනට මැසේජ් එකක් විතරක් දෙමු)
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}