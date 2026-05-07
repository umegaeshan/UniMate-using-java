package com.example.unimate;

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

        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // Navigation Bar කේතය
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_completed) {
                // Completed පිටුවට යන කේතය පසුව ලියමු, දැනට මැසේජ් එකක් පෙන්වමු
                Toast.makeText(TaskListActivity.this, "Completed Tasks Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            }  else if (id == R.id.nav_dev) {
            // Dev Info බොත්තම එබුවම මේ අලුත් පිටුවට යනවා
            startActivity(new Intent(getApplicationContext(), DevInfoActivity.class));
            overridePendingTransition(0, 0);
            return true;
            } else if (id == R.id.nav_profile) {
                Toast.makeText(TaskListActivity.this, "Profile Page Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        fetchTasksFromFirebase();
    }

    private void fetchTasksFromFirebase() {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Toast.makeText(TaskListActivity.this, "Error loading tasks", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        myTasks.clear();

                        for (DocumentSnapshot doc : value.getDocuments()) {
                            TaskModel task = doc.toObject(TaskModel.class);
                            if (task != null) {
                                task.taskId = doc.getId();
                                myTasks.add(task);
                            }
                        }

                        taskAdapter.notifyDataSetChanged();
                    }
                });
    }
}