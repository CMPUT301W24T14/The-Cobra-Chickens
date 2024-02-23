package com.example.eventplanner;

import java.util.Date;

public class Notification {
    private String title;
    private String message;
    private Date date;
    private boolean read;

    public Notification(String title, String message, Date date, boolean read) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.read = read;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
