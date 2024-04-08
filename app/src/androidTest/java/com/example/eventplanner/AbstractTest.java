package com.example.eventplanner;
import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.List;

public abstract class AbstractTest {

    @Before
    public void splashScreenContinue() {
        try {
            onView(withText("Continue")).perform(click());
        } catch (Exception ignored) {}

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clickAllow();
    }
    @After
    public void after() {
        try {
            setNameToTest();
        } catch (Exception ignored) {}
    }

    public void clickAllow() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            // Wait for the permission dialog to appear
            UiObject allowButton = uiDevice.findObject(new UiSelector().text("Allow"));
            if (allowButton.exists()) {
                allowButton.click();
            }
        } catch (Exception ignored) {}

        try {
            // Wait for the permission dialog to appear
            UiObject allowButton = uiDevice.findObject(new UiSelector().text("While using the app"));
            if (allowButton.exists()) {
                allowButton.click();
            }
        } catch (Exception ignored) {}

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
    }

    public void setNameToTest() {
        goToProfile();
        onView(withText("edit details")).perform(click());
        onView(withId(R.id.saveBtn)).check(matches(isDisplayed()));

        onView(withId(R.id.editName)).perform(replaceText("Testing"));
        onView(withId(R.id.editContact)).perform(replaceText("Testing"));
        onView(withId(R.id.editHomepage)).perform(replaceText("Testing"));
        onView(withId(R.id.saveBtn)).perform(click());
    }

    public Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection<Activity> allActivities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED);
                if (!allActivities.isEmpty()) {
                    currentActivity[0] = allActivities.iterator().next();
                }
            }
        });
        return currentActivity[0];
    }

    public void goToAllEvents() {
        onView(withId(R.id.home)).perform(click());
        onView(withText("All Events")).check(matches(isDisplayed()));
        onView(withText("All Events")).perform(click());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToMyEvents() {
        onView(withId(R.id.home)).perform(click());
        onView(withText("My Events")).check(matches(isDisplayed()));
        onView(withText("My Events")).perform(click());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToOrganize() {
        onView(withId(R.id.home)).perform(click());
        onView(withText("Organize")).check(matches(isDisplayed()));
        onView(withText("Organize")).perform(click());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToNotifications() {
        onView(withId(R.id.notifications)).perform(click());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToProfile() {
        onView(withId(R.id.profile)).perform(click());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchBarSearch(String name) {
        // Wait for the permission dialog to appear
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Search Events"));
        assert textField.exists();
        if (textField.exists()) {
            try {
                textField.click();
            } catch (UiObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                textField.setText(name);
            } catch (UiObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
            uiDevice.pressEnter();
        }
    }

    public void clickOn(String name) {
        // Wait for the permission dialog to appear
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text(name));
        assert textField.exists();
        if (textField.exists()) {
            try {
                textField.click();
            } catch (UiObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void clickOn(String name, int instanceNumber) {
        // Wait for the permission dialog to appear
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        List<UiObject2> instances = uiDevice.findObjects(By.text(name));

        if (instances.size() >= instanceNumber) {
            instances.get(instanceNumber - 1).click();
        } else assert false;
    }

    public void textVisible(String text) {
        // Wait for the permission dialog to appear
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text(text));
        assert textField.exists();
    }

    public void addPhotoSequence() {
        // Wait for the permission dialog to appear
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Photos"));
        assert textField.exists();
        try {
            textField.click();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Find the text "Yesterday"
        UiObject textPhotos = uiDevice.findObject(new UiSelector().text("Photos"));

        // Get the bounds of the text "Yesterday"
        android.graphics.Rect bounds = null;
        try {
            bounds = textPhotos.getBounds();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Calculate the center position below the text
        int centerX = bounds.centerX() - 250;
        int centerY = bounds.bottom + 250; // Adjust the Y-coordinate as needed

        // Click on the picture at the calculated position
        uiDevice.click(centerX, centerY);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void typeThisOnThat(String content, String toFind) {
        // Wait for the permission dialog to appear
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text(toFind));
        assert textField.exists();
        if (textField.exists()) {
            try {
                textField.click();
            } catch (UiObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                textField.setText(content);
            } catch (UiObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
            uiDevice.pressBack();
        }
    }

    public void clickOnCheckInGeolocation() {
        // Find the text "verification"
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Track Check-in Geolocation"));
        assert textField.exists();

        // Get the bounds of the text "verification"
        android.graphics.Rect bounds = null;
        try {
            bounds = textField.getBounds();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Calculate the center position below the text
        int centerX = uiDevice.getDisplayWidth() - 100;
        int centerY = bounds.centerY();

        // Click on the switch at the calculated position
        uiDevice.click(centerX, centerY);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectDate() {
        // Find the text "verification"
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Please Select a Date"));
        assert textField.exists();
        try {
            textField.click();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        textField = uiDevice.findObject(new UiSelector().text("OK"));
        assert textField.exists();
        try {
            textField.click();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectTime() {
        // Find the text "verification"
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Please Select a Time"));
        assert textField.exists();
        try {
            textField.click();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        textField = uiDevice.findObject(new UiSelector().text("OK"));
        assert textField.exists();
        try {
            textField.click();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void swipe() {
        // Initialize UiDevice instance
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Set the swipe starting and ending points
        int startX = uiDevice.getDisplayWidth() / 2;   // middle of the screen
        int endY = uiDevice.getDisplayHeight() / 2;  // center of the screen
        int endX = startX;
        int startY = uiDevice.getDisplayHeight() - 200; // a little above the bottom of the screen

        // Perform the swipe gesture to scroll down
        uiDevice.swipe(startX, startY, endX, endY, 10); // 10 steps for smooth scrolling

    }

    public void createEvent(String eventName, String eventDescription, String numberAttendees, String eventLocation, Boolean trackGeolocation) {
        clickOn("Create Event");
        typeThisOnThat(eventName, "Enter Event Name");
        typeThisOnThat(eventDescription, "Enter Event Description");
        if (numberAttendees != null) typeThisOnThat(numberAttendees, "Max Number of Attendees (Optional)");
        typeThisOnThat(eventLocation, "Enter Event Location");
        if (trackGeolocation) clickOnCheckInGeolocation();
        swipe();
        selectDate();
        selectTime();
        swipe();
        clickOn("Upload Image");
        addPhotoSequence();
        swipe();
        onView(withId(R.id.btn_create_event)).perform(click());
        onView(withId(R.id.btn_create_event)).perform(click());
        onView(withId(R.id.btn_create_event)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject text = uiDevice.findObject(new UiSelector().text("All Events"));
        while (!text.exists()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
            }
        }

    }
}
