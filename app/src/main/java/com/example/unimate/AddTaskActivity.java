package com.example.unimate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    EditText etTaskName, etDescription, etSelectedDate, etSelectedTime;
    Button btnPriority, btnCategory, btnAddTask;

    String selectedPriority = "Medium Priority", selectedCategory = "Other";
    int priorityLevel = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etSelectedDate = findViewById(R.id.etSelectedDate);
        etSelectedTime = findViewById(R.id.etSelectedTime);
        btnPriority = findViewById(R.id.btnPriority);
        btnCategory = findViewById(R.id.btnCategory);
        btnAddTask = findViewById(R.id.btnAdd);

        // 1. Date Picker (Native)
        etSelectedDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                String date = String.format(Locale.getDefault(), "%02d/%02d/%d", day, (month + 1), year);
                etSelectedDate.setText(date);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 2. Time Picker (Native)
        etSelectedTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                String amPm = (hour >= 12) ? "PM" : "AM";
                int h = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
                String time = String.format(Locale.getDefault(), "%02d:%02d %s", h, minute, amPm);
                etSelectedTime.setText(time);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
        });

        // 3. Priority Menu
        btnPriority.setOnClickListener(v -> {
            PopupMenu p = new PopupMenu(this, btnPriority);
            p.getMenu().add("High Priority"); p.getMenu().add("Medium Priority"); p.getMenu().add("Low Priority");
            p.setOnMenuItemClickListener(item -> {
                selectedPriority = item.getTitle().toString();
                btnPriority.setText(selectedPriority);
                priorityLevel = selectedPriority.equals("High Priority") ? 1 : (selectedPriority.equals("Medium Priority") ? 2 : 3);
                return true;
            });
            p.show();
        });

        // 4. Category Menu
        btnCategory.setOnClickListener(v -> {
            PopupMenu p = new PopupMenu(this, btnCategory);
            String[] mods = {"Instrumentation", "Automation Systems", "PCB Design", "Programming", "Network Engineering"};
            for (String m : mods) p.getMenu().add(m);
            p.setOnMenuItemClickListener(item -> {
                selectedCategory = item.getTitle().toString();
                btnCategory.setText(selectedCategory);
                return true;
            });
            p.show();
        });

        // 5. Add Task to Firebase
        btnAddTask.setOnClickListener(v -> {
            String name = etTaskName.getText().toString().trim();
            String date = etSelectedDate.getText().toString().trim();

            if (name.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill Task Name and Date", Toast.LENGTH_SHORT).show();
                return;
            }
            saveTask();
        });
    }

    private void saveTask() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        TaskModel task = new TaskModel(
                etTaskName.getText().toString(),
                etDescription.getText().toString(),
                false,
                selectedCategory,
                selectedPriority,
                priorityLevel,
                etSelectedDate.getText().toString(),
                etSelectedTime.getText().toString()
        );

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("tasks").add(task).addOnSuccessListener(ref -> finish());
    }
}