package com.example.eventplanner;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

// OpenAI, 2024, ChatGPT (version GPT-3.5). https://chat.openai.com/chat
public class EventUnitTest {

    private Event event;

    @Before
    public void setUp() {
        // Initialize an Event object with sample data for testing
        String eventId = "1";
        String eventName = "Test Event";
        String eventDescription = "Description";
        String eventMaxAttendees = "100";
        String eventDate = "2024-04-10";
        String eventTime = "09:00 AM";
        String eventLocation = "Test Location";
        String eventPoster = "https://example.com/poster.jpg";
        String checkInCode = "1234";
        String promoCode = "PROMO123";
        ArrayList<String> eventAnnouncements = new ArrayList<>();
        eventAnnouncements.add("Announcement 1");
        ArrayList<CheckedInUser> checkedInUsers = new ArrayList<>();
        ArrayList<String> signedUpUsers = new ArrayList<>();
        signedUpUsers.add("user1");

        event = new Event(eventId, eventName, eventDescription, eventMaxAttendees, eventDate, eventTime, eventLocation, eventPoster,
                checkInCode, promoCode, eventAnnouncements, checkedInUsers, signedUpUsers);
    }

    @Test
    public void testEventConstructor() {
        // Verify that the Event object is not null
        assertNotNull(event);

        // Verify that the fields are initialized correctly
        assertEquals("1", event.getEventId());
        assertEquals("Test Event", event.getEventName());
        assertEquals("Description", event.getEventDescription());
        assertEquals("100", event.getEventMaxAttendees());
        assertEquals("2024-04-10", event.getEventDate());
        assertEquals("09:00 AM", event.getEventTime());
        assertEquals("Test Location", event.getEventLocation());
        assertEquals("https://example.com/poster.jpg", event.getEventPoster());
        assertEquals("1234", event.getCheckInCode());
        assertEquals("PROMO123", event.getPromoCode());
        assertEquals(1, event.getEventAnnouncements().size());
        assertEquals("Announcement 1", event.getEventAnnouncements().get(0));
        assertEquals(0, event.getCheckedInUsers().size());
        assertEquals("user1", event.getSignedUpUsers().get(0));

        // Add assertions for other fields if needed
    }

    @Test
    public void testSettersAndGetters() {
        // Verify the setters and getters for each field
        event.setEventId("2");
        assertEquals("2", event.getEventId());

        event.setEventName("Updated Event Name");
        assertEquals("Updated Event Name", event.getEventName());

        // Add assertions for other fields if needed
    }

    @Test
    public void testCheckedInUsersNotNull() {
        // Verify that the checked-in users list is initialized and not null
        assertNotNull(event.getCheckedInUsers());
    }

    @Test
    public void testSignedUpUsersNotNull() {
        // Verify that the signed-up users list is initialized and not null
        assertNotNull(event.getSignedUpUsers());
    }

    @Test
    public void testEventAnnouncementsNotNull() {
        // Verify that the event announcements list is initialized and not null
        assertNotNull(event.getEventAnnouncements());
    }
}