package com.example.eventplanner;
/**
 * Class representing a notification.
 * This class encapsulates the title and message of a notification.
 */
public class MyNotification {
    private String title;
    private String message;

    // Default constructor
    /**
     * Default constructor.
     * Constructs an empty MyNotification object.
     */
    public MyNotification() {
        // Default initialization if needed
    }

    // Constructor for all fields
    /**
     * Constructor with parameters.
     * Constructs a MyNotification object with the specified title and message.
     * @param title The title of the notification.
     * @param message The message content of the notification.
     */
    public MyNotification(String title, String message) {
        this.title = title;
        this.message = message;
    }
    /**
     * Get the title of the notification.
     * @return The title of the notification.
     */
    // Getters and setters
    public String getTitle() {
        return title;
    }
    /**
     * Set the title of the notification.
     * @param title The title of the notification.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Get the message content of the notification.
     * @return The message content of the notification.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Set the message content of the notification.
     * @param message The message content of the notification.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
