package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 1. බොත්තම සඳහා විචල්‍යයක් (නමක්) හදාගන්නවා
    Button btnLetsStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2. අපි කලින් හදපු XML ඩිසයින් එක මේකට සම්බන්ධ කරනවා
        setContentView(R.layout.activity_main);

        // 3. XML එකේ තිබ්බ බොත්තම මේ නමට සම්බන්ධ කරනවා (ID එක හරහා)
        btnLetsStart = findViewById(R.id.btnLetsStart);

        // 4. බොත්තම එබුවම මොකද වෙන්න ඕනේ කියලා ලියනවා
        btnLetsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // බොත්තම එබුවම ඊළඟ පිටුවට යන්න Intent එකක් පාවිච්චි කරනවා.
                // (දැනට LoginActivity කියලා පිටුවක් නැති නිසා මේක රතු පාටින් පෙන්නයි, ඒකට බයවෙන්න එපා)
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}