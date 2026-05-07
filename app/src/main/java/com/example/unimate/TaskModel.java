package com.example.unimate;

public class TaskModel {

    // ටාස්ක් එකක තියෙන විස්තර (Variables)
    String title;
    String description;
    boolean isCompleted; // වැඩේ ඉවරද නැද්ද කියලා බලන්න

    // Constructor (මේ අච්චුවෙන් ටාස්ක් එකක් හදද්දී දෙන විස්තර)
    public TaskModel(String title, String description, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Getters (ඇතුලේ තියෙන විස්තර එළියට ගන්න පාවිච්චි කරන ක්‍රම)
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