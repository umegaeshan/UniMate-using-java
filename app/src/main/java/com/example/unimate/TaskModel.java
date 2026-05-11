package com.example.unimate;

import com.google.firebase.firestore.DocumentId;

public class TaskModel {
    @DocumentId
    public String taskId;
    public String title, description, category, priority, dateText, timeText;
    public boolean isCompleted;
    public int priorityLevel;

    public TaskModel() {} // Firebase සඳහා

    public TaskModel(String title, String description, boolean isCompleted, String category, String priority, int priorityLevel, String dateText, String timeText) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.category = category;
        this.priority = priority;
        this.priorityLevel = priorityLevel;
        this.dateText = dateText;
        this.timeText = timeText;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return isCompleted; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public int getPriorityLevel() { return priorityLevel; }
    public String getDateText() { return dateText; }
    public String getTimeText() { return timeText; }
}