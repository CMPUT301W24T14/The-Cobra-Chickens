package com.example.eventplanner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// OpenAI, 2024, ChatGPT (version GPT-3.5). https://chat.openai.com/chat
public class PushNotificationUnitTest {

    private PushNotification pushNotification;
    private NotificationData notificationData;

    @Before
    public void setUp() {
        notificationData = new NotificationData("Test Title", "Test Message");
        pushNotification = new PushNotification(notificationData, "testToken");
    }

    @Test
    public void testConstructor() {
        assertNotNull(pushNotification);
    }

    @Test
    public void testGetData() {
        assertEquals(notificationData, pushNotification.getData());
    }

    @Test
    public void testSetData() {
        NotificationData newData = new NotificationData("New Title", "New Message");
        pushNotification.setData(newData);
        assertEquals(newData, pushNotification.getData());
    }

    @Test
    public void testGetTo() {
        assertEquals("testToken", pushNotification.getTo());
    }

    @Test
    public void testSetTo() {
        pushNotification.setTo("newToken");
        assertEquals("newToken", pushNotification.getTo());
    }
}