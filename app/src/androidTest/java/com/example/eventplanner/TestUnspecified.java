package com.example.eventplanner;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUnspecified {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);
    // Test Splash Screen
    /*@Test
    public void test_00_Splash() {
        onView(withText("Welcome to Cobra Event Management")).check(matches(isDisplayed()));
        onView(withText("Continue")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withText("Welcome to Cobra Event Management")).check(doesNotExist());
    }*/

    @Test
    public void test_01_MainScreen() {
        onView(withText("All Events")).check(matches(isDisplayed()));
        onView(withText("My Events")).check(matches(isDisplayed()));
        onView(withText("Organize")).check(matches(isDisplayed()));
    }
}