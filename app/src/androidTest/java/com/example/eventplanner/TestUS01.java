package com.example.eventplanner;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUS01 {
    // Temporary test to make this class not give an error when running without other tests
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.eventplanner", appContext.getPackageName());
    }

    // Tests for US 01.01.01
    // As an organizer, I want to create a new event and generate a unique QR code for attendee check-ins.
    @Test
    public void test01_01_01() {
        assert(false);
    }

    // Tests for US 01.01.02
    // As an organizer, I want the option to reuse an existing QR code for attendee check-ins.
    /*
    @Test
    public void test01_01_02() {

    }
     */

    // Tests for US 01.02.01
    // As an organizer, I want to view the list of attendees who have checked in to my event.
    /*
    @Test
    public void test01_02_01() {

    }
     */

    // Tests for US 01.03.01
    // As an organizer, I want to send notifications to all attendees through the app.
    /*
    @Test
    public void test01_03_01() {

    }
     */

    // Tests for US 01.04.01
    // As an organizer, I want to upload an event poster to provide visual information to attendees.
    /*
    @Test
    public void test01_04_01() {

    }
     */

    // Tests for US 01.05.01
    // As an organizer, I want to track real-time attendance and receive alerts for important milestones.
    /*
    @Test
    public void test01_05_01() {

    }
     */

    // Tests for US 01.06.01
    // As an organizer, I want to share a generator QR code image to other apps so I can email or update other documents with the QR code.
    /*
    @Test
    public void test01_06_01() {

    }
     */

    // Tests for US 01.07.01
    // As an organizer, I want to create a new event and generate a unique promotion QR code that links to the event description and event poster in the app.
    /*
    @Test
    public void test01_07_01() {

    }
     */

    // Tests for US 01.08.01
    // As an organizer, I want to see on a map where users are checking in from.
    /*
    @Test
    public void test01_08_01() {

    }
     */

    // Tests for US 01.09.01
    // As an organizer, I want to see how many times an attendee has checked into an event.
    /*
    @Test
    public void test01_09_01() {

    }
     */

    // Tests for US 01.10.01
    // As an organizer, I want to see who is signed up to attend my event.
    /*
    @Test
    public void test01_10_01() {

    }
     */

    // Tests for US 01.11.01
    // As an organizer, I want to OPTIONALLY limit the number of attendees that can sign up for an event.
    /*
    @Test
    public void test01_11_01() {

    }
     */
}
