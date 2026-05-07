package com.example.unimate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Firebase වැඩ වලට අවශ්‍ය දේවල්
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<TaskModel> taskList;

    public TaskAdapter(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel currentTask = taskList.get(position);

        holder.tvTaskTitle.setText(currentTask.getTitle());
        holder.tvTaskDesc.setText(currentTask.getDescription());

        // කලින් සේව් වෙලා තියෙන විදිහට හරි ලකුණ දානවා හෝ අයින් කරනවා
        // මේක හරිම වැදගත්: Listener එක දාන්න කලින් අපි මේක Set කරන්න ඕනේ. නැත්නම් කේතය පැටලෙනවා.
        holder.checkCompleted.setOnCheckedChangeListener(null);
        holder.checkCompleted.setChecked(currentTask.isCompleted());

        // 1. Firebase සම්බන්ධතා මෙතනට ගන්නවා
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 2. Checkbox එක (හරි ලකුණ) එබුවම වෙන දේ
        holder.checkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Firebase එකේ තියෙන මේ Task එක හොයාගෙන, ඒකෙ 'isCompleted' කියන එක Update කරනවා
            db.collection("users").document(userId).collection("tasks")
                    .document(currentTask.getTaskId())
                    .update("isCompleted", isChecked);
        });

        // 3. Delete (කුණු කූඩය) එබුවම වෙන දේ
        holder.imgDelete.setOnClickListener(v -> {
            // Firebase එකෙන් ඒ අදාළ Task එක සදහටම මකලා (Delete) දානවා
            db.collection("users").document(userId).collection("tasks")
                    .document(currentTask.getTaskId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskDesc;
        CheckBox checkCompleted;
        ImageView imgDelete; // අලුතින් ආපු කුණු කූඩය

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDesc = itemView.findViewById(R.id.tvTaskDesc);
            checkCompleted = itemView.findViewById(R.id.checkCompleted);
            imgDelete = itemView.findViewById(R.id.imgDelete); // XML එකේ අයිකන් එක අල්ලගන්නවා
        }
    }
}