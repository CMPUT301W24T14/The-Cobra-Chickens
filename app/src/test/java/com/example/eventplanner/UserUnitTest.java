package com.example.eventplanner;

import android.os.Parcel;

import com.example.eventplanner.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

// OpenAI, 2024, ChatGPT (version GPT-3.5). https://chat.openai.com/chat
public class UserUnitTest {
    private User user;

    @Before
    public void setUp() {
        String userId = "123";
        String name = "John Doe";
        String homepage = "https://example.com";
        String contactInformation = "john@example.com";
        String profilePicture = "https://example.com/profile.jpg";
        Boolean geolocationTrackingEnabled = true;
        ArrayList<String> signedUpForEventList = new ArrayList<>();
        ArrayList<String> organizingEventsList = new ArrayList<>();
        ArrayList<String> checkedInEventsList = new ArrayList<>();
        ArrayList<String> reusableCodes = new ArrayList<>();
        user = new User(userId, name, homepage, contactInformation, profilePicture,
                geolocationTrackingEnabled, signedUpForEventList, organizingEventsList,
                checkedInEventsList, reusableCodes);
    }

    @Test
    public void testGetters() {
        assertEquals("123", user.getUserId());
        assertEquals("John Doe", user.getName());
        assertEquals("https://example.com", user.getHomepage());
        assertEquals("john@example.com", user.getContactInformation());
        assertEquals("https://example.com/profile.jpg", user.getProfilePicture());
        assertTrue(user.getGeolocationTrackingEnabled());
        assertEquals(0, user.getSignedUpForEventList().size());
        assertEquals(0, user.getOrganizingEventsList().size());
        assertEquals(0, user.getCheckedInEventsList().size());
        assertEquals(0, user.getReusableCodes().size());
    }

    @Test
    public void testSetters() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());

        user.setHomepage("https://example.org");
        assertEquals("https://example.org", user.getHomepage());

        user.setContactInformation("jane@example.com");
        assertEquals("jane@example.com", user.getContactInformation());

        user.setProfilePicture("https://example.org/profile.jpg");
        assertEquals("https://example.org/profile.jpg", user.getProfilePicture());

        user.setGeolocationTrackingEnabled(false);
        assertFalse(user.getGeolocationTrackingEnabled());

        ArrayList<String> eventList = new ArrayList<>();
        eventList.add("Event 1");
        eventList.add("Event 2");

        user.setSignedUpForEventList(eventList);
        assertEquals(2, user.getSignedUpForEventList().size());
    }

    @Test
    public void testConstructor() {
        User newUser = new User("456", "Jane Smith", "https://example.org", "jane@example.org", "https://example.org/profile.jpg");
        assertEquals("456", newUser.getUserId());
        assertEquals("Jane Smith", newUser.getName());
        assertEquals("https://example.org", newUser.getHomepage());
        assertEquals("jane@example.org", newUser.getContactInformation());
        assertEquals("https://example.org/profile.jpg", newUser.getProfilePicture());
    }

}
