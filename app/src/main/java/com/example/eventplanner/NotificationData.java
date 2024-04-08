package com.example.eventplanner;

/**
 * Represents the data (a title and a message) that is contained in a push notification.
 */
public class NotificationData {
    private String title;
    private String message;

    public NotificationData(String title, String message) {
        this.title = title;
        this.message = message;
    }

    /**
     * Retrieves the title of the notification.
     * @return The title of the notification.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the notification.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the message of the notification.
     * @return The message of the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the notification.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
