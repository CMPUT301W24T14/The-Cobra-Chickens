package com.example.eventplanner;

public class MyNotification {
    private String title;
    private String message;

    // Default constructor
    public MyNotification() {
        // Default initialization if needed
    }

    // Constructor for all fields
    public MyNotification(String title, String message) {
        this.title = title;
        this.message = message;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
