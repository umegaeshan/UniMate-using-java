package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvHeader, tvDetail, tvPriority, tvStatus;
    TabLayout tabLayout;
    Button btnComplete;
    ImageView btnDelete, btnEdit;

    String taskId, title, desc, date, category, priority;
    boolean isCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Catching the data passed from the TaskAdapter via Intent
        taskId = getIntent().getStringExtra("taskId");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        date = getIntent().getStringExtra("date");
        category = getIntent().getStringExtra("category");
        priority = getIntent().getStringExtra("priority");
        isCompleted = getIntent().getBooleanExtra("isCompleted", false);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvHeader = findViewById(R.id.tvContentHeader);
        tvDetail = findViewById(R.id.tvContentDetail);
        tvPriority = findViewById(R.id.tvDetailPriority);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tabLayout = findViewById(R.id.detailTabLayout);
        btnComplete = findViewById(R.id.btnMarkComplete);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit); // Linking the edit button

        // Setting up initial UI values
        tvTitle.setText(title);
        tvDetail.setText(desc);
        tvPriority.setText(priority);
        tvStatus.setText(isCompleted ? "Completed" : "Pending");

        // Logic to switch displayed content when different tabs are clicked
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selected = tab.getText().toString();
                tvHeader.setText(selected);
                if (selected.equals("Description")) tvDetail.setText(desc);
                else if (selected.equals("Due Date")) tvDetail.setText(date);
                else tvDetail.setText(category);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 1. Logic to mark a task as completed and update the database
        btnComplete.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("tasks").document(taskId)
                    .update("isCompleted", true)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Task Completed!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
        });

        // 2. Logic to delete a task directly from the details page
        btnDelete.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("tasks").document(taskId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
        });

        // 3. Logic to pass current task data to the Edit screen
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskActivity.class);
            intent.putExtra("taskId", taskId);
            intent.putExtra("title", title);
            intent.putExtra("desc", desc);
            intent.putExtra("date", date);
            intent.putExtra("category", category);
            intent.putExtra("priority", priority);
            startActivity(intent);
        });

        // 4. Integrating bottom navigation on the detail page
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, TaskListActivity.class));
                    finish(); return true;
                } else if (id == R.id.nav_completed) {
                    startActivity(new Intent(this, CompletedTasksActivity.class));
                    finish(); return true;
                } else if (id == R.id.nav_dev) {
                    startActivity(new Intent(this, DevInfoActivity.class));
                    finish(); return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish(); return true;
                }
                return false;
            });
        }
    }
}