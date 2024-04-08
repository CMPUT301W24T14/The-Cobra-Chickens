package com.example.eventplanner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// OpenAI, 2024, ChatGPT (version GPT-3.5). https://chat.openai.com/chat
public class NotificationDataUnitTest {

    private NotificationData notificationData;

    @Before
    public void setUp() {
        notificationData = new NotificationData("Test Title", "Test Message");
    }

    @Test
    public void testConstructor() {
        assertNotNull(notificationData);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test Title", notificationData.getTitle());
    }

    @Test
    public void testSetTitle() {
        notificationData.setTitle("New Title");
        assertEquals("New Title", notificationData.getTitle());
    }

    @Test
    public void testGetMessage() {
        assertEquals("Test Message", notificationData.getMessage());
    }

    @Test
    public void testSetMessage() {
        notificationData.setMessage("New Message");
        assertEquals("New Message", notificationData.getMessage());
    }
}
