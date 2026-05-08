package com.example.unimate;

import java.util.Collections;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    RecyclerView recyclerViewTasks;
    TaskAdapter taskAdapter;
    List<TaskModel> myTasks;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        myTasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(myTasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        findViewById(R.id.fabAddTask).setOnClickListener(v -> {
            startActivity(new Intent(TaskListActivity.this, AddTaskActivity.class));
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dev) {
                startActivity(new Intent(getApplicationContext(), DevInfoActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_completed) {
            startActivity(new Intent(getApplicationContext(), CompletedTasksActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
            return id == R.id.nav_home;
        });

        fetchTasksFromFirebase();
    }

    private void fetchTasksFromFirebase() {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        // මෙහිදී .whereEqualTo("isCompleted", false) ඇතුළත් කර ඇත
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