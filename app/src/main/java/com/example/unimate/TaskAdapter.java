package com.example.unimate;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.tvDisplayDate.setText(currentTask.getDateText());
        holder.tvDisplayTime.setText(currentTask.getTimeText());

        if (currentTask.getCategory() != null) {
            holder.tvCategoryBadge.setText(currentTask.getCategory());
        }

        if (currentTask.getPriority() != null) {
            holder.tvPriorityBadge.setText(currentTask.getPriority());
            if (currentTask.getPriorityLevel() == 1) {
                holder.tvPriorityBadge.setBackgroundColor(Color.parseColor("#D32F2F"));
            } else if (currentTask.getPriorityLevel() == 2) {
                holder.tvPriorityBadge.setBackgroundColor(Color.parseColor("#F57C00"));
            } else {
                holder.tvPriorityBadge.setBackgroundColor(Color.parseColor("#388E3C"));
            }
        }

        holder.checkCompleted.setOnCheckedChangeListener(null);
        holder.checkCompleted.setChecked(currentTask.isCompleted());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.checkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            db.collection("users").document(userId).collection("tasks")
                    .document(currentTask.getTaskId())
                    .update("isCompleted", isChecked);
        });

        holder.imgDelete.setOnClickListener(v -> {
            db.collection("users").document(userId).collection("tasks")
                    .document(currentTask.getTaskId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() { return taskList.size(); }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskDesc, tvPriorityBadge, tvCategoryBadge, tvDisplayDate, tvDisplayTime;
        CheckBox checkCompleted;
        ImageView imgDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDesc = itemView.findViewById(R.id.tvTaskDesc);
            tvPriorityBadge = itemView.findViewById(R.id.tvPriorityBadge);
            tvCategoryBadge = itemView.findViewById(R.id.tvCategoryBadge);
            tvDisplayDate = itemView.findViewById(R.id.tvDisplayDate);
            tvDisplayTime = itemView.findViewById(R.id.tvDisplayTime);
            checkCompleted = itemView.findViewById(R.id.checkCompleted);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}