package com.example.eventplanner;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUS02 {

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
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void test02_02_03_00() {
        onView(withId(R.id.profile)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withText("edit details")).check(matches(isDisplayed()));
        onView(withId(R.id.home)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withText("edit details")).check(doesNotExist());
    }
    @Test
    public void test02_02_03_01() {
        onView(withId(R.id.profile)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withText("edit details")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.saveBtn)).check(matches(isDisplayed()));

        onView(withId(R.id.editName)).perform(replaceText("Mark Zukerberg"));
        onView(withId(R.id.editContact)).perform(replaceText("zuck@fb.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://www.facebook.com/zuck/"));
        onView(withId(R.id.saveBtn)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.profileName)).check(matches(withText("Name:Mark Zukerberg")));
        onView(withId(R.id.profileContact)).check(matches(withText("Contact:zuck@fb.com")));
        onView(withId(R.id.profileHomepage)).check(matches(withText("Homepage:https://www.facebook.com/zuck/")));
    }
    @Test
    public void test02_02_03_02() {
        onView(withId(R.id.profile)).perform(click());

        onView(withText("edit details")).perform(click());

        onView(withId(R.id.saveBtn)).check(matches(isDisplayed()));

        onView(withId(R.id.editName)).perform(replaceText("Timothée Chalamet"));
        onView(withId(R.id.editContact)).perform(replaceText("timchal@gmail.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://www.facebook.com/timotheechalamet95/"));
        onView(withId(R.id.saveBtn)).perform(click());

        onView(withId(R.id.home)).perform(click());

        onView(withId(R.id.profile)).perform(click());

        onView(withId(R.id.profileName)).check(matches(withText("Name: Timothée Chalamet")));
        onView(withId(R.id.profileContact)).check(matches(withText("Contact: timchal@gmail.com")));
        onView(withId(R.id.profileHomepage)).check(matches(withText("Homepage: https://www.facebook.com/timotheechalamet95/")));

        onView(withText("edit details")).perform(click());

        onView(withId(R.id.editName)).perform(replaceText("Mark Zukerberg"));
        onView(withId(R.id.editContact)).perform(replaceText("zuck@fb.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://www.facebook.com/zuck/"));
        onView(withId(R.id.saveBtn)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }

        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profileName)).check(matches(withText("Name: Mark Zukerberg")));
        onView(withId(R.id.profileContact)).check(matches(withText("Contact: zuck@fb.com")));
        onView(withId(R.id.profileHomepage)).check(matches(withText("Homepage: https://www.facebook.com/zuck/")));

    }

    // Tests for US 02.03.01
    // As an attendee, I want to receive push notifications with important updates from the event organizers.
    /*
    @Test
    public void test02_03_01() {

    }
     */

    // Tests for US 02.04.01
    // As an attendee, I want to view event details and announcements within the app.
    @Test
    public void test02_04_01() {
        onView(withId(R.id.home)).perform(click());
        onView(withText("All Events")).check(matches(isDisplayed()));
        onView(withText("All Events")).perform(click());
        onView(withText("Event1")).perform(click());
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withText("Sign Up")).perform(click());
        onView(withText(("Confirm"))).perform(click());
        Espresso.pressBack();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withId(R.id.home)).perform(click());
        onView(withText("My Events")).check(matches(isDisplayed()));
        onView(withText("My Events")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        onView(withText("Event1")).perform(click());
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withText("Announcements:")).check(matches(isDisplayed()));
    }

    // Tests for US 02.05.01
    // As an attendee, I want my profile picture to be deterministically generated from my profile name if I haven't uploaded an profile image yet.
    /*
    @Test
    public void test02_05_01() {

    }
     */

    // Tests for US 02.06.01
    // As an attendee, I do not want to login to the app. No username, no password.
    @Test
    public void test02_06_01() {
        onView(withText("All Events")).check(matches(isDisplayed()));
        onView(withText("My Events")).check(matches(isDisplayed()));
        onView(withText("Organize")).check(matches(isDisplayed()));
    }

    // Tests for US 02.07.01
    // As an attendee, I want to sign up to attend an event from the event details (as in I promise to go).
    /*
    @Test
    public void test02_07_01() {

    }
     */

    // Tests for US 02.08.01
    // As an attendee, I want to browse event posters/event details of other events.
    @Test
    public void test02_08_01() {
        onView(withId(R.id.home)).perform(click());
        onView(withText("All Events")).check(matches(isDisplayed()));
        onView(withText("All Events")).perform(click());
        onView(withText("Event1")).perform(click());
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withText("Announcements:")).check(matches(isDisplayed()));
    }

    // Tests for US 02.09.01
    // As an attendee, I want to know what events I signed up for currently and in and in the future.
    /*
    @Test
    public void test02_09_01() {

    }
     */
}
