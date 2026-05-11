package com.example.unimate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {

    EditText etTitle, etDesc;
    Button btnUpdate;
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task); // Add Task ඩිසයින් එකම ගන්නවා

        // අදාළ පෙට්ටි සහ බොත්තම අල්ලගැනීම
        etTitle = findViewById(R.id.etTaskName);
        etDesc = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnAdd);

        btnUpdate.setText("Update Task"); // බොත්තමේ නම Add වෙනුවට Update ලෙස වෙනස් කරනවා

        // TaskDetailActivity එකෙන් එන පරණ දත්ත පෙට්ටි වලට දානවා (Pre-fill)
        taskId = getIntent().getStringExtra("taskId");
        etTitle.setText(getIntent().getStringExtra("title"));
        etDesc.setText(getIntent().getStringExtra("desc"));

        // Update බොත්තම එබූ විට අලුත් දත්ත Firebase එකට යැවීම
        btnUpdate.setOnClickListener(v -> {
            String newTitle = etTitle.getText().toString();
            String newDesc = etDesc.getText().toString();

            if (newTitle.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("title", newTitle);
            map.put("description", newDesc);

            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("tasks").document(taskId)
                    .update(map)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Task Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // සාර්ථකව Update වුණාම ආපහු පරණ පිටුවට යනවා
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error updating task", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}