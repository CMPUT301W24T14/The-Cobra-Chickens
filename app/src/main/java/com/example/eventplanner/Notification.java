package com.example.eventplanner;

import java.time.LocalDateTime;

public class Notification {
    private String title; // The title of the notification
    private String message; // The detailed message of the notification
    private LocalDateTime dateTime; // The date and time when the notification was created or should be shown
    private boolean read; // Flag to check if the notification has been read

    // Constructor
    public Notification(String title, String message, LocalDateTime dateTime, boolean read) {
        this.title = title;
        this.message = message;
        this.dateTime = dateTime;
        this.read = read;
    }

    // Getters and Setters
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
