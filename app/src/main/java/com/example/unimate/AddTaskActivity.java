package com.example.unimate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// Firebase වෙනුවෙන් ගෙනාපු අලුත් දේවල්
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    // 1. XML එකේ තියෙන හැම පෙට්ටියකටම නම් හඳුන්වා දෙනවා
    TextView tvSelectedDate, tvSelectedTime;
    EditText etTaskName, etTaskDesc;
    Button btnSubmitTaskFinal;

    // Firebase මුරකාරයා (Auth) සහ අලුත් පොත (Firestore)
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // 2. Firebase දේවල් වැඩට ගන්නවා
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 3. XML එකේ තියෙන දේවල් කේතයට සම්බන්ධ කරනවා
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        etTaskName = findViewById(R.id.etTaskName);
        etTaskDesc = findViewById(R.id.etTaskDesc);
        btnSubmitTaskFinal = findViewById(R.id.btnSubmitTaskFinal);

        // (අපි කලින් ලියපු Date සහ Time පෙන්වන කේත ටික මෙතන ඒ විදිහටම තියෙනවා)
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvSelectedDate.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tvSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                                int hour = (hourOfDay > 12) ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);
                                tvSelectedTime.setText(String.format("%02d : %02d %s", hour, minute, amPm));
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        // 4. "Add Task" ප්‍රධාන බොත්තම එබුවම වෙන දේ ලියමු
        btnSubmitTaskFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // පෙට්ටි වල තියෙන වචන ටික ඇදලා අරගන්නවා
                String taskName = etTaskName.getText().toString();
                String taskDesc = etTaskDesc.getText().toString();
                String taskDate = tvSelectedDate.getText().toString();
                String taskTime = tvSelectedTime.getText().toString();

                // විස්තර මොනවාහරි අඩුයි නම් එපා කියනවා
                if (taskName.isEmpty() || taskDesc.isEmpty() || taskDate.equals("Enter date") || taskTime.equals("Enter time")) {
                    Toast.makeText(AddTaskActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                    return; // මෙතනින් කේතය නවත්තනවා
                }

                // දැනට ලොග් වෙලා ඉන්න ළමයාගේ ID එක හොයාගන්නවා (එතකොට එයාගේ දේවල් වෙනම සේව් කරන්න පුළුවන්)
                String userId = mAuth.getCurrentUser().getUid();

                // පාර්සලයක් (Map) හදලා යවන්න ඕනේ විස්තර ටික ඒකට දානවා
                Map<String, Object> task = new HashMap<>();
                task.put("title", taskName);
                task.put("description", taskDesc);
                task.put("date", taskDate);
                task.put("time", taskTime);
                task.put("isCompleted", false); // අලුතින් දාන වැඩක් කොහොමත් ඉවර කරලා නැහැනේ, ඒකයි මේක false කරන්නේ

                // අර පාර්සලය Firebase එකට යවනවා
                db.collection("users").document(userId).collection("tasks")
                        .add(task)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> taskResult) {
                                if (taskResult.isSuccessful()) {
                                    Toast.makeText(AddTaskActivity.this, "Task Added Successfully!", Toast.LENGTH_SHORT).show();
                                    finish(); // වැඩේ හරි ගියාට පස්සේ මේ පිටුව වහලා ආපහු Home (Task List) එකට යවනවා
                                } else {
                                    Toast.makeText(AddTaskActivity.this, "Error: " + taskResult.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}