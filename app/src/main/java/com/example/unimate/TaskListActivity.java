package com.example.unimate;

import java.util.Collections;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    RecyclerView recyclerViewTasks;
    TaskAdapter taskAdapter;
    List<TaskModel> myTasks;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    // Required variables for the search functionality
    boolean isSearchVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // Must load the UI first

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        myTasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(myTasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        // 1. Settings icon to handle dark mode toggling
        ImageView btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Settings");
                int currentMode = AppCompatDelegate.getDefaultNightMode();
                boolean isDarkMode = (currentMode == AppCompatDelegate.MODE_NIGHT_YES);
                String[] options = {isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode"};

                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
                    }
                });
                builder.show();
            });
        }

        // 2. Notification icon and red dot badge logic
        ImageView btnNotification = findViewById(R.id.btnNotification);
        View redDotBadge = findViewById(R.id.redDotBadge);

        if (btnNotification != null && redDotBadge != null) {
            redDotBadge.setVisibility(View.VISIBLE); // Simulating a new notification
            btnNotification.setOnClickListener(v -> {
                redDotBadge.setVisibility(View.GONE); // Hiding the badge on click
                Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show();
            });
        }

        // 3. Search icon and input field logic
        EditText etSearch = findViewById(R.id.etSearch);
        ImageView btnSearch = findViewById(R.id.btnSearch);

        if (btnSearch != null && etSearch != null) {
            btnSearch.setOnClickListener(v -> {
                if (isSearchVisible) {
                    etSearch.setVisibility(View.GONE);
                    etSearch.setText("");
                } else {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }
                isSearchVisible = !isSearchVisible;
            });

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    filterTasks(s.toString());
                }
            });
        }

        findViewById(R.id.fabAddTask).setOnClickListener(v -> {
            startActivity(new Intent(TaskListActivity.this, AddTaskActivity.class));
        });

        // Navigation setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            else if (id == R.id.nav_completed) {
                startActivity(new Intent(TaskListActivity.this, CompletedTasksActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_dev) {
                startActivity(new Intent(TaskListActivity.this, DevInfoActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(TaskListActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            }
            return false;
        });

        fetchTasksFromFirebase();
    }

    // Method to handle real-time search filtering (placed outside onCreate)
    private void filterTasks(String text) {
        List<TaskModel> filteredList = new ArrayList<>();
        for (TaskModel task : myTasks) {
            if (task.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    (task.getCategory() != null && task.getCategory().toLowerCase().contains(text.toLowerCase()))) {
                filteredList.add(task);
            }
        }
        taskAdapter.setFilteredList(filteredList);
    }

    private void fetchTasksFromFirebase() {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("tasks")
                .whereEqualTo("isCompleted", false)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    myTasks.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        TaskModel task = doc.toObject(TaskModel.class);
                        if (task != null) {
                            task.taskId = doc.getId();
                            myTasks.add(task);
                        }
                    }
                    Collections.sort(myTasks, (t1, t2) -> Integer.compare(t1.getPriorityLevel(), t2.getPriorityLevel()));
                    taskAdapter.notifyDataSetChanged();
                });
    }
}