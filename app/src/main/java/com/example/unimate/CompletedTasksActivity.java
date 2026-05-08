package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        recyclerView = findViewById(R.id.recyclerViewCompleted);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedTasks = new ArrayList<>();
        adapter = new TaskAdapter(completedTasks);
        recyclerView.setAdapter(adapter);

        fetchCompletedTasks();
        setupNavigation();
    }

    private void fetchCompletedTasks() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        FirebaseFirestore.getInstance().collection("users").document(userId).collection("tasks")
                .whereEqualTo("isCompleted", true) // මෙහිදී true ඒවා පමණක් ගනී
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
                startActivity(new Intent(this, TaskListActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return id == R.id.nav_completed;
        });
    }
}