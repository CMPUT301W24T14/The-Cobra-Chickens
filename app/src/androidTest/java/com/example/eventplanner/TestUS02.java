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
public class TestUS02 {
    // Temporary test to make this class not give an error when running without other tests
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.eventplanner", appContext.getPackageName());
    }

    // Tests for US 02.01.01
    // As an attendee, I want to quickly check into an event by scanning the provided QR code.
    /*
    @Test
    public void test02_01_01() {

    }
     */

    // Tests for US 02.02.01
    // As an attendee, I want to upload a profile picture for a more personalized experience.
    /*
    @Test
    public void test02_02_01() {

    }
     */

    // Tests for US 02.02.02
    // As an attendee, I want to remove profile pictures if need be.
    /*
    @Test
    public void test02_02_02() {

    }
     */

    // Tests for US 02.02.03
    // As an attendee, I want to update information such as name, homepage, and contact information on my profile.
    /*
    @Test
    public void test02_02_03() {

    }
     */

    // Tests for US 02.03.01
    // As an attendee, I want to receive push notifications with important updates from the event organizers.
    /*
    @Test
    public void test02_03_01() {

    }
     */

    // Tests for US 02.04.01
    // As an attendee, I want to view event details and announcements within the app.
    /*
    @Test
    public void test02_04_01() {

    }
     */

    // Tests for US 02.05.01
    // As an attendee, I want my profile picture to be deterministically generated from my profile name if I haven't uploaded an profile image yet.
    /*
    @Test
    public void test02_05_01() {

    }
     */

    // Tests for US 02.06.01
    // As an attendee, I do not want to login to the app. No username, no password.
    /*
    @Test
    public void test02_06_01() {

    }
     */

    // Tests for US 02.07.01
    // As an attendee, I want to sign up to attend an event from the event details (as in I promise to go).
    /*
    @Test
    public void test02_07_01() {

    }
     */

    // Tests for US 02.08.01
    // As an attendee, I want to browse event posters/event details of other events.
    /*
    @Test
    public void test02_08_01() {

    }
     */

    // Tests for US 02.09.01
    // As an attendee, I want to know what events I signed up for currently and in and in the future.
    /*
    @Test
    public void test02_09_01() {

    }
     */
}
