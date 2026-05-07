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
// Firebase අදාළ දේවල්
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

    // Firebase දේවල් හඳුන්වා දීම
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // 1. Firebase මුරකාරයාවයි පොතයි වැඩට ගන්නවා
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        // 2. හිස් ලිස්ට් එකක් හදලා ඒක Adapter (පාලම) හරහා තිරයට සම්බන්ධ කරනවා
        myTasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(myTasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        // 3. යට තියෙන '+' බොත්තම එබුවම Add Task පිටුවට යන කේතය
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // 4. Firebase එකෙන් දත්ත ඇදලා ගන්න අපි හදපු ක්‍රමය (Method) මෙතනින් කෝල් කරනවා
        fetchTasksFromFirebase();
    }

    // Firebase එකෙන් දත්ත ඇදලා ගන්නා ක්‍රමය
    private void fetchTasksFromFirebase() {
        // කවුරුහරි ලොග් වෙලා නැත්නම් මේක කරන්න එපා කියලා කියනවා
        if (mAuth.getCurrentUser() == null) return;

        // ලොග් වෙලා ඉන්න ළමයාගේ ID එක ගන්නවා (එයාගේ ෆෝල්ඩරේ හොයාගන්න)
        String userId = mAuth.getCurrentUser().getUid();

        // Database එකේ ඒ ළමයාගේ "tasks" කියන ෆෝල්ඩරේ දිහා බලාගෙන ඉන්නවා (addSnapshotListener)
        db.collection("users").document(userId).collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        // මොකක්හරි අවුලක් (Error) ආවොත් මැසේජ් එකක් දෙනවා
                        if (error != null) {
                            Toast.makeText(TaskListActivity.this, "Error loading tasks", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // පරණ ලිස්ට් එකේ දේවල් තියෙනවා නම් ඒක හිස් කරනවා (නැත්නම් එකම ටාස්ක් එක දෙපාරක් පෙනෙයි)
                        myTasks.clear();

                        // Firebase එකෙන් ආපු විස්තර එකින් එක අරගෙන අපේ අච්චුවට (TaskModel) දාලා ලිස්ට් එකට එකතු කරනවා
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            TaskModel task = doc.toObject(TaskModel.class);
                            myTasks.add(task);
                        }

                        // "අලුත් දේවල් ආවා, තිරය වෙනස් කරන්න" කියලා පාලමට (Adapter) දැනුම් දෙනවා
                        taskAdapter.notifyDataSetChanged();
                    }
                });
    }
}