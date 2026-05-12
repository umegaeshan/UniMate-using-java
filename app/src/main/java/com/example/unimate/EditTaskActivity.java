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

        // Reusing the Add Task layout here because the input form looks exactly the same
        setContentView(R.layout.activity_add_task);

        // Initializing UI components
        etTitle = findViewById(R.id.etTaskName);
        etDesc = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnAdd);

        btnUpdate.setText("Update Task"); // Changing button text since we are in edit mode

        // Fetching the passed intent data and pre-filling the fields for the user
        taskId = getIntent().getStringExtra("taskId");
        etTitle.setText(getIntent().getStringExtra("title"));
        etDesc.setText(getIntent().getStringExtra("desc"));

        // Sending the updated data back to Firestore when the button is clicked
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
                        finish(); // Closing the edit screen to go back
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error updating task", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}