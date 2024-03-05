package com.example.eventplanner;

import java.util.Date;

public class Notification {
    String title; // The title of the notification
    String message; // The detailed message of the notification
    Date date; // The date when the notification was created or should be shown
    boolean isRead; // Flag to check if the notification has been read

    // Constructor
    public Notification(String title, String message, Date date, boolean isRead) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}