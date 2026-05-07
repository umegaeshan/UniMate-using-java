package com.example.unimate;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    RecyclerView recyclerViewTasks;
    TaskAdapter taskAdapter;
    List<TaskModel> myTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // 1. XML එකේ තියෙන RecyclerView එක හොයාගන්නවා
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // 2. ලිස්ට් එක පේන්න ඕනේ උඩ ඉඳන් පහළට කියලා කියනවා
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        // 3. අපේ දත්ත (Tasks) දාන්න හිස් ලිස්ට් එකක් හදාගන්නවා
        myTasks = new ArrayList<>();

        // 4. ටෙස්ට් කරන්න බොරු දත්ත ටිකක් ඇතුල් කරනවා (අච්චුව පාවිච්චි කරලා)
        myTasks.add(new TaskModel("Programming I", "Complete the assignment questions", false));
        myTasks.add(new TaskModel("Application Lab", "Create the student models and routes", true));
        myTasks.add(new TaskModel("Maths", "Do the tutorial 03", false));

        // 5. අපි හදපු පාලමට (Adapter) අපේ දත්ත ටික දෙනවා
        taskAdapter = new TaskAdapter(myTasks);

        // 6. පාලම අරන් ගිහින් RecyclerView එකට සවි කරනවා
        recyclerViewTasks.setAdapter(taskAdapter);
    }
}