package com.example.unimate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// මේකෙන් තමයි කියන්නේ මේක Adapter එකක් කියලා
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // අපි පෙන්වන්න ඕනේ වැඩ (Tasks) ලිස්ට් එක මෙතනට ගන්නවා
    List<TaskModel> taskList;

    public TaskAdapter(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    // 1. අර අපි කලින් හදපු ඩිසයින් එක (item_task.xml) මෙතනට ගේනවා
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // 2. අච්චුවේ තියෙන දත්ත (Data) ටික අරගෙන ඩිසයින් එකේ තියෙන පෙට්ටි වලට දානවා
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // දැනට ලිස්ට් එකේ තියෙන අංකය (position) අනුව අදාළ ටාස්ක් එක ගන්නවා
        TaskModel currentTask = taskList.get(position);

        // ඒ ටාස්ක් එකේ මාතෘකාව අරන් TextView එකට දානවා
        holder.tvTaskTitle.setText(currentTask.getTitle());
        // විස්තරය අරන් අනිත් TextView එකට දානවා
        holder.tvTaskDesc.setText(currentTask.getDescription());
        // හරි ලකුණ (Tick) දානවද නැද්ද කියලා තීරණය කරනවා
        holder.checkCompleted.setChecked(currentTask.isCompleted());
    }

    // 3. ලිස්ට් එකේ ඔක්කොම ටාස්ක් කීයක් තියෙනවද කියලා ඇන්ඩ්‍රොයිඩ් එකට කියනවා
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder: XML ඩිසයින් එකේ තියෙන කෑලි ටික කේතයට අඳුන්වලා දෙන තැන
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskDesc;
        CheckBox checkCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDesc = itemView.findViewById(R.id.tvTaskDesc);
            checkCompleted = itemView.findViewById(R.id.checkCompleted);
        }
    }
}