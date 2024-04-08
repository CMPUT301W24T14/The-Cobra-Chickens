package com.example.eventplanner;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.android.material.internal.ContextUtils.getActivity;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

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
public class TestUS02 extends AbstractTest {
    @Rule
    public ActivityScenarioRule<SplashScreen> scenario = new ActivityScenarioRule<SplashScreen>(SplashScreen.class);
    // Tests for US 02.01.01
    // As an attendee, I want to quickly check into an event by scanning the provided QR code.
    // N/A

    // Tests for US 02.02.01
    // As an attendee, I want to upload a profile picture for a more personalized experience.
    // Tests for US 02.02.02
    // As an attendee, I want to remove profile pictures if need be.
    @Test
    public void test02_02_01_and_02_02_02() {
        goToProfile();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        Activity currentActivity = getCurrentActivity();
        ImageView pfp = currentActivity.findViewById(R.id.profilePic);
        Drawable im = pfp.getDrawable();

        clickOn("edit");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        addPhotoSequence();
        ImageView pfpNew = currentActivity.findViewById(R.id.profilePic);
        assert(pfpNew.getDrawable() != im);

        pfp = pfpNew;
        clickOn("delete");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }

        pfpNew = currentActivity.findViewById(R.id.profilePic);
        assert(pfpNew.getDrawable() != im);

    }

    // Tests for US 02.02.03
    // As an attendee, I want to update information such as name, homepage, and contact information on my profile.
    @Test
    public void test02_02_03_00() {
        goToProfile();
        onView(withText("edit details")).check(matches(isDisplayed()));
        goToAllEvents();
        onView(withText("edit details")).check(doesNotExist());
    }
    @Test
    public void test02_02_03_01() throws InterruptedException {
        goToProfile();
        onView(withText("edit details")).perform(click());
        Thread.sleep(200);
        onView(withId(R.id.saveBtn)).check(matches(isDisplayed()));

        onView(withId(R.id.editName)).perform(replaceText("Mark Zukerberg"));
        onView(withId(R.id.editContact)).perform(replaceText("zuck@fb.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://www.facebook.com/zuck/"));
        onView(withId(R.id.saveBtn)).perform(click());
        Thread.sleep(200);
        onView(withId(R.id.profileName)).check(matches(withText("Name:Mark Zukerberg")));
        onView(withId(R.id.profileContact)).check(matches(withText("Contact:zuck@fb.com")));
        onView(withId(R.id.profileHomepage)).check(matches(withText("Homepage:https://www.facebook.com/zuck/")));
    }
    @Test
    public void test02_02_03_02() {
        goToProfile();
        onView(withText("edit details")).perform(click());
        onView(withId(R.id.saveBtn)).check(matches(isDisplayed()));

        onView(withId(R.id.editName)).perform(replaceText("Timothée Chalamet"));
        onView(withId(R.id.editContact)).perform(replaceText("timchal@gmail.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://www.facebook.com/timotheechalamet95/"));
        onView(withId(R.id.saveBtn)).perform(click());

        goToAllEvents();
        goToProfile();

        onView(withId(R.id.profileName)).check(matches(withText("Name: Timothée Chalamet")));
        onView(withId(R.id.profileContact)).check(matches(withText("Contact: timchal@gmail.com")));
        onView(withId(R.id.profileHomepage)).check(matches(withText("Homepage: https://www.facebook.com/timotheechalamet95/")));

        onView(withText("edit details")).perform(click());

        onView(withId(R.id.editName)).perform(replaceText("Mark Zukerberg"));
        onView(withId(R.id.editContact)).perform(replaceText("zuck@fb.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://www.facebook.com/zuck/"));
        onView(withId(R.id.saveBtn)).perform(click());

        goToAllEvents();
        goToProfile();
        onView(withId(R.id.profileName)).check(matches(withText("Name: Mark Zukerberg")));
        onView(withId(R.id.profileContact)).check(matches(withText("Contact: zuck@fb.com")));
        onView(withId(R.id.profileHomepage)).check(matches(withText("Homepage: https://www.facebook.com/zuck/")));

    }

    // Tests for US 02.03.01
    // As an attendee, I want to receive push notifications with important updates from the event organizers.
    @Test
    public void test02_03_01() {
        goToNotifications();
        textVisible("Notifications");
    }

    // Tests for US 02.04.01
    // As an attendee, I want to view event details and announcements within the app.
    @Test
    public void test02_04_01() {
        goToAllEvents();
        searchBarSearch("Math Meme");
        clickOn("Math Meme Review");
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withText("Announcements:")).check(matches(isDisplayed()));
    }

    // Tests for US 02.05.01
    // As an attendee, I want my profile picture to be deterministically generated from my profile name if I haven't uploaded an profile image yet.
    @Test
    public void test02_05_01() {
        goToProfile();
        onView(withText("edit details")).check(matches(isDisplayed()));
        Activity currentActivity = getCurrentActivity();
        ImageView pfp = currentActivity.findViewById(R.id.profilePic);
        Drawable im = pfp.getDrawable();

        onView(withText("edit details")).perform(click());
        onView(withId(R.id.saveBtn)).check(matches(isDisplayed()));

        onView(withId(R.id.editName)).perform(replaceText("Daaaannnn"));
        onView(withId(R.id.editContact)).perform(replaceText("daannn@gmail.com"));
        onView(withId(R.id.editHomepage)).perform(replaceText("https://dandandan.com"));
        onView(withId(R.id.saveBtn)).perform(click());

        ImageView pfpNew = currentActivity.findViewById(R.id.profilePic);
        assert(pfpNew.getDrawable() != im);
    }

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
    // Tests for US 02.09.01
    // As an attendee, I want to know what events I signed up for currently and in the future.
    @Test
    public void test02_07_01_and_02_09_01() {
        goToAllEvents();
        searchBarSearch("Math Meme");
        clickOn("Math Meme Review");

        clickOn("Sign up");
        clickOn("Confirm");
        Espresso.pressBack();

        goToMyEvents();
        searchBarSearch("Math Meme");
        clickOn("Math Meme Review");
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withText("Announcements:")).check(matches(isDisplayed()));
    }

    // Tests for US 02.08.01
    // As an attendee, I want to browse event posters/event details of other events.
    @Test
    public void test02_08_01() {
        goToAllEvents();
        searchBarSearch("Math Meme");
        clickOn("Math Meme Review");
        onView(withText("Event Details")).check(matches(isDisplayed()));
        onView(withText("Announcements:")).check(matches(isDisplayed()));
    }
}
