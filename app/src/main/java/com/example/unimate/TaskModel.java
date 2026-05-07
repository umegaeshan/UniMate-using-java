package com.example.unimate;

public class TaskModel {

    String title;
    String description;
    boolean isCompleted;

    // 1. Firebase එකට දත්ත පුරවන්න ඕනේ කරන "හිස් අච්චුව" (අලුතින් එකතු කළ කොටස)
    public TaskModel() {
    }

    // 2. සාමාන්‍ය අච්චුව (කලින් තිබ්බ එක)
    public TaskModel(String title, String description, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
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