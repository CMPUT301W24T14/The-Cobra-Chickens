package com.example.eventplanner;

import java.util.Date;

public class Notification {
    private String title;
    private String message;
    private Date date; // Ensure this is using java.util.Date
    private boolean read;

    // Constructor
    public Notification(String title, String message, Date date, boolean read) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.read = read;
    }

    // Getter for the title
    public String getTitle() {
        return title;
    }

    // Setter for the title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for the message
    public String getMessage() {
        return message;
    }

    // Setter for the message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for the date
    public Date getDate() {
        return date;
    }

    // Setter for the date
    public void setDate(Date date) {
        this.date = date;
    }

    // Getter for the read status
    public boolean isRead() {
        return read;
    }

    // Setter for the read status
    public void setRead(boolean read) {
        this.read = read;
    }
}
