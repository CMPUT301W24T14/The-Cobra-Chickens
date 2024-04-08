package com.example.eventplanner;

import org.junit.Test;
import static org.junit.Assert.*;

// OpenAI, 2024, ChatGPT (version GPT-3.5). https://chat.openai.com/chat
public class MyNotificationUnitTest {

    @Test
    public void testDefaultConstructor() {
        // Test default constructor
        MyNotification notification = new MyNotification();
        assertNull(notification.getTitle());
        assertNull(notification.getMessage());
    }

    @Test
    public void testParameterizedConstructor() {
        // Test parameterized constructor
        MyNotification notification = new MyNotification("Title", "Message");
        assertEquals("Title", notification.getTitle());
        assertEquals("Message", notification.getMessage());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters and getters
        MyNotification notification = new MyNotification();
        notification.setTitle("New Title");
        notification.setMessage("New Message");
        assertEquals("New Title", notification.getTitle());
        assertEquals("New Message", notification.getMessage());
    }
}
