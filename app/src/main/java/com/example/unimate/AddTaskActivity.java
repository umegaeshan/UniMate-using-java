package com.example.unimate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    // 1. අපි XML එකේ හදපු Text දෙකට නම් හඳුන්වා දෙනවා
    TextView tvSelectedDate, tvSelectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // 2. XML එකේ තියෙන දේවල් මේ නම් වලට සම්බන්ධ කරනවා (ID එකෙන්)
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);

        // 3. Date (දිනය) තෝරන "Enter date" කොටස ක්ලික් කළාම වෙන දේ
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ෆෝන් එකේ දැනට තියෙන දිනය හොයාගන්නවා (කැලැන්ඩරය මුලින්ම ඒ දවසින් ඇරෙන්න ඕනේ නිසා)
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialog (කැලැන්ඩර කොටුව) පෙන්වනවා
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // කෙනෙක් දිනය තේරුවට පස්සේ ඒක ලස්සනට හදලා Text එකට දානවා
                                // (පරිගණකයේ මාස ගණන් කරන්නේ 0 ඉඳන් නිසා අපි මාසයට 1ක් එකතු කරනවා)
                                String selectedDate = dayOfMonth + " / " + (monthOfYear + 1) + " / " + year;
                                tvSelectedDate.setText(selectedDate);
                            }
                        },
                        year, month, day);

                // හැදූ කැලැන්ඩරය තිරයේ පෙන්වන්න අණ දෙනවා
                datePickerDialog.show();
            }
        });

        // 4. Time (වේලාව) තෝරන "Enter time" කොටස ක්ලික් කළාම වෙන දේ
        tvSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ෆෝන් එකේ දැනට තියෙන වේලාව හොයාගන්නවා
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // TimePickerDialog (ඔරලෝසු කොටුව) පෙන්වනවා
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // වේලාව AM ද PM ද කියලා හොයාගන්න පොඩි ගණිතයක් කරනවා
                                String amPm;
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                    if (hourOfDay > 12) hourOfDay -= 12; // උදා: 13, 14 වෙනුවට 1, 2 කරන්න
                                } else {
                                    amPm = "AM";
                                    if (hourOfDay == 0) hourOfDay = 12; // රෑ 12ට 0 වෙනුවට 12 පෙන්නන්න
                                }

                                // තෝරපු වේලාව ලස්සනට හදලා Text එකට දානවා
                                // String.format වලින් %02d පාවිච්චි කරන්නේ ඉලක්කම් 2ක් අනිවාර්යයෙන් පෙන්වන්න (උදා: 5 වෙනුවට 05)
                                String selectedTime = String.format("%02d : %02d %s", hourOfDay, minute, amPm);
                                tvSelectedTime.setText(selectedTime);
                            }
                        },
                        hour, minute, false); // අන්තිමට තියෙන false එකෙන් කියන්නේ පැය 24 රටාව එපා, AM/PM රටාව ඕනේ කියන එකයි

                // හැදූ ඔරලෝසුව තිරයේ පෙන්වන්න අණ දෙනවා
                timePickerDialog.show();
            }
        });
    }
}