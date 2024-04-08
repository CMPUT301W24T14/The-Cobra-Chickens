package com.example.eventplanner;

import java.util.Date;
//NOT IN USE CURRENTLY
public class Notification {
    private String title;
    private String message;
    private Date date;

    // Constructor for all fields
    public Notification(String title, String message, Date date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }
    public Notification(){

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
