package com.example.unimate;

// ID එක අල්ලගන්න අවශ්‍ය අලුත් කේතය
import com.google.firebase.firestore.DocumentId;

public class TaskModel {

    // 1. Firebase එකෙන් දෙන ID එක සේව් කරගන්න තැන (මේක ගොඩක් වැදගත්!)
    @DocumentId
    String taskId;

    String title;
    String description;
    boolean isCompleted;

    // හිස් අච්චුව
    public TaskModel() {
    }

    // සාමාන්‍ය අච්චුව
    public TaskModel(String title, String description, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // දත්ත එළියට ගන්න ක්‍රම (Getters)
    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}