package com.example.eventplanner;
import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

public abstract class AbstractTest {

    @Before
    public void splashScreenContinue() {
        try {
            onView(withText("Continue")).perform(click());
        } catch (Exception e) {}

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }

        try {
            // Simulate clicking on the permission dialog button to accept the permission
            UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            // Wait for the permission dialog to appear
            UiObject allowButton = uiDevice.findObject(new UiSelector().text("Allow"));
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

    public void goToOrganizeEvents() {
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
        // Simulate clicking on the permission dialog button to accept the permission
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Wait for the permission dialog to appear
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
        // Simulate clicking on the permission dialog button to accept the permission
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Wait for the permission dialog to appear
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

    public void textVisible(String text) {
        // Simulate clicking on the permission dialog button to accept the permission
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Wait for the permission dialog to appear
        UiObject textField = uiDevice.findObject(new UiSelector().text(text));
        assert textField.exists();
    }

    public void addPhotoSequence() {
        // Simulate clicking on the permission dialog button to accept the permission
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Wait for the permission dialog to appear
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
}
