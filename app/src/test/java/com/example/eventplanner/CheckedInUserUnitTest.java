package com.example.eventplanner;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

// OpenAI, 2024, ChatGPT (version GPT-3.5). https://chat.openai.com/chat
public class CheckedInUserUnitTest {
    @Test
    public void testConstructorAndGetters() {
        // Test constructor and getters
        CheckedInUser user = new CheckedInUser("123", "5");
        assertEquals("123", user.getUserId());
        assertEquals("5", user.getNumberOfCheckins());
    }

    @Test
    public void testSetters() {
        // Test setters
        CheckedInUser user = new CheckedInUser("123", "5");
        user.setUserId("456");
        user.setNumberOfCheckins("10");
        assertEquals("456", user.getUserId());
        assertEquals("10", user.getNumberOfCheckins());
    }
}

