package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class CompletedTasksActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TaskAdapter adapter;
    List<TaskModel> completedTasks;
    boolean isSearchVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        recyclerView = findViewById(R.id.recyclerViewCompleted);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedTasks = new ArrayList<>();
        adapter = new TaskAdapter(completedTasks);
        recyclerView.setAdapter(adapter);

        // Handling the settings icon click to toggle light/dark theme
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

        // Handling the notification icon click
        ImageView btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        // Implementing a dynamic search bar to filter completed tasks
        EditText etSearch = findViewById(R.id.etSearch);
        ImageView btnSearch = findViewById(R.id.btnSearch);

        if (btnSearch != null && etSearch != null) {
            btnSearch.setOnClickListener(v -> {
                // Toggling search bar visibility when the search icon is clicked
                if (isSearchVisible) {
                    etSearch.setVisibility(View.GONE);
                    etSearch.setText("");
                } else {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }
                isSearchVisible = !isSearchVisible;
            });

            // Updating the task list in real-time as the user types
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    filterTasks(s.toString());
                }
            });
        }

        fetchCompletedTasks();
        setupNavigation();
    }

    private void filterTasks(String text) {
        List<TaskModel> filteredList = new ArrayList<>();
        for (TaskModel task : completedTasks) {
            // Checking if the search keyword matches the task title or category
            if (task.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    (task.getCategory() != null && task.getCategory().toLowerCase().contains(text.toLowerCase()))) {
                filteredList.add(task);
            }
        }
        adapter.setFilteredList(filteredList);
    }

    private void fetchCompletedTasks() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        // Querying Firestore to get only tasks that are marked as completed
        FirebaseFirestore.getInstance().collection("users").document(userId).collection("tasks")
                .whereEqualTo("isCompleted", true)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    completedTasks.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        TaskModel task = doc.toObject(TaskModel.class);
                        if (task != null) {
                            task.taskId = doc.getId();
                            completedTasks.add(task);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void setupNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_completed);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(CompletedTasksActivity.this, TaskListActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_completed) return true;
            else if (id == R.id.nav_dev) {
                startActivity(new Intent(CompletedTasksActivity.this, DevInfoActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(CompletedTasksActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            }
            return false;
        });
    }
}