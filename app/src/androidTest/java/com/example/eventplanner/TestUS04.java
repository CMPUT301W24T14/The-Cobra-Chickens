package com.example.eventplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.ContentView;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUS04 extends AbstractTest {
    @Rule
    public ActivityScenarioRule<AdminActivity> scenario = new ActivityScenarioRule<AdminActivity>(AdminActivity.class);

    @Before
    public void setUp() {
        /*try {
            Thread.sleep(50000000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }*/
    }

    // Tests for US 04.01.01
    // As an administrator, I want to be able to remove events.
    /* Disabled test to prevent removing something important **/
    /*
    @Test
    public void test04_01_01() {
        onView(withText("See Events")).check(matches(isDisplayed()));
        onView(withText("See Events")).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.adminEventsRecyclerView)).perform(click());
        onView(withText("YES")).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
    }
    */

    // Tests for US 04.02.01
    // As an administrator, I want to be able to remove profiles.
    /* Disabled test to prevent removing something important ***/
    /*@Test
    public void test04_02_01() {
        onView(withText("See Profiles")).check(matches(isDisplayed()));
        onView(withText("See Profiles")).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.adminProfilesRecyclerView)).perform(click());
        onView(withText("YES")).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
    }*/

    // Tests for US 04.03.01
    // As an administrator, I want to be able to remove images.
    /* Disabled test to prevent removing something important ***/
    /*
    @Test
    public void test04_03_01() {
        onView(withText("See Profile Pictures")).check(matches(isDisplayed()));
        onView(withText("See Profile Pictures")).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.adminProPicsRecyclerView)).perform(click());
        onView(withText("YES")).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
        Espresso.pressBack();

        onView(withText("See Event Posters")).check(matches(isDisplayed()));
        onView(withText("See Event Posters")).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.adminPostersRecyclerView)).perform(click());
        onView(withText("YES")).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());
        Espresso.pressBack();
    }*/

    // Tests for US 04.04.01
    // As an administrator, I want to be able to browse events.
    @Test
    public void test04_04_01() {
        onView(withText("See Events")).check(matches(isDisplayed()));
        onView(withText("See Events")).perform(click());
    }

    // Tests for US 04.05.01
    // As an administrator, I want to be able to browse profiles.
    @Test
    public void test004_05_01() {
        onView(withText("See Profiles")).check(matches(isDisplayed()));
        onView(withText("See Profiles")).perform(click());
    }


    // Tests for US 04.06.01
    // As an administrator, I want to be able to browse images.
    @Test
    public void test04_06_01() {
        clickOn("See Profile Pictures");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        Espresso.pressBack();

        clickOn("See Event Posters");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        Espresso.pressBack();
    }
}
