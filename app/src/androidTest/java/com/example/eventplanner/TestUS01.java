package com.example.eventplanner;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUS01 extends AbstractTest {
    @Rule
    public ActivityScenarioRule<SplashScreen> scenario = new ActivityScenarioRule<SplashScreen>(SplashScreen.class);

    // Tests for US 01.01.01
    // As an organizer, I want to create a new event and generate a unique QR code for attendee check-ins.
    // Tests for US 01.02.01
    // As an organizer, I want to view the list of attendees who have checked in to my event.
    // Tests for US 01.04.01
    // As an organizer, I want to upload an event poster to provide visual information to attendees.
    // Tests for US 01.07.01
    // As an organizer, I want to create a new event and generate a unique promotion QR code that links to the event description and event poster in the app.
    // Tests for US 01.08.01
    // As an organizer, I want to see on a map where users are checking in from.
    // Tests for US 01.10.01
    // As an organizer, I want to see who is signed up to attend my event.
    @Test
    public void test01_01_01_and_01_02_01_and_01_04_01_and_01_07_01_and_01_08_01_and_01_10_01() {
        goToOrganize();
        createEvent("Testing US 01.01.01",
                "As an organizer, I want to create a new event and generate a unique QR code for attendee check-ins.",
                null,
                "N/A",
                true);

        searchBarSearch("01.01.01");
        clickOn("Testing US 01.01.01");
        swipe();
        clickOn("Generate Check-in QR Code");
        clickOn("GENERATE NEW QR");
        swipe();
        clickOn("Generate Promo QR Code");
        swipe();
        swipe();
        swipe();
        textVisible("Number of checked-in attendees: 0");
        clickOn("Attendee Check-in Map");
        clickAllow();

        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject text = uiDevice.findObject(new UiSelector().text("Back"));
        while (text.exists()) {
            try {
                clickOn("Back");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
            }
        }

        Espresso.pressBack();
        goToAllEvents();
        searchBarSearch("01.01.01");
        clickOn("Testing US 01.01.01");
        swipe();
        swipe();
        clickOn("Sign up");
        clickOn("Confirm");
        Espresso.pressBack();

        setNameToTest();

        goToOrganize();
        searchBarSearch("01.01.01");
        clickOn("Testing US 01.01.01");
        swipe();

        textVisible("Testing");
        Espresso.pressBack();

    }

    // Tests for US 01.01.02
    // As an organizer, I want the option to reuse an existing QR code for attendee check-ins.
    /*
    @Test
    public void test01_01_02() {
        splashScreenContinue();
    }
     */


    // Tests for US 01.03.01
    // As an organizer, I want to send notifications to all attendees through the app.
    /*
    @Test
    public void test01_03_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 01.05.01
    // As an organizer, I want to track real-time attendance and receive alerts for important milestones.
    /*
    @Test
    public void test01_05_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 01.06.01
    // As an organizer, I want to share a generator QR code image to other apps so I can email or update other documents with the QR code.
    /*
    @Test
    public void test01_06_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 01.09.01
    // As an organizer, I want to see how many times an attendee has checked into an event.
    // N/A --> requires someone to scan QR code to check-in (which requires to test use of camera).
    /*
    @Test
    public void test01_09_01() {
        splashScreenContinue();
    }
     */


    // Tests for US 01.11.01
    // As an organizer, I want to OPTIONALLY limit the number of attendees that can sign up for an event.
    @Test
    public void test01_11_01() {
        goToOrganize();
        createEvent("Testing US 01.11.01",
                "As an organizer, I want to OPTIONALLY limit the number of attendees that can sign up for an event.",
                "0",
                "N/A",
                false);

        goToAllEvents();
        searchBarSearch("01.11.01");
        clickOn("Testing US 01.11.01");
        swipe();
        swipe();
        clickOn("Sign up");
        clickOn("Confirm");
        Espresso.pressBack();

        goToMyEvents();
        searchBarSearch("01.11.01");

        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject text = uiDevice.findObject(new UiSelector().text("Testing US 01.11.01"));
        assert !text.exists();

    }
}
