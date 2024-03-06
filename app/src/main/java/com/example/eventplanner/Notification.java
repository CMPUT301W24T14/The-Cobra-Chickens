package com.example.eventplanner;

import java.util.Date;

public class Notification {
    String title; // The title of the notification
    String message; // The detailed message of the notification
    Date date; // The date when the notification was created or should be shown
    boolean isRead; // Flag to check if the notification has been read

    // Constructor for a notification with only a message
    public Notification(String message) {
        this.message = message;
        // Set default values for title, date, and read status
        this.title = ""; // Default title (can be removed if not needed)
        this.date = new Date(); // Set the current date as default (can be removed if not needed)
        this.isRead = false; // Default read status
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public boolean isRead() {
        return isRead;
    }

}
