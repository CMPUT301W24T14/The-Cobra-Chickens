package com.example.eventplanner;
import static androidx.test.espresso.action.ViewActions.click;
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
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUS03 extends AbstractTest {
    @Rule
    public ActivityScenarioRule<SplashScreen> scenario = new ActivityScenarioRule<SplashScreen>(SplashScreen.class);
    // Temporary test to make this class not give an error when running without other tests

    // Tests for US 03.02.01
    // As a user, I want the option to enable or disable geolocation tracking for event verification.
    // Attendee
    @Test
    public void test03_02_01_01() {
        goToProfile();

        // Find the text "verification"
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Not currently tracking"));
        assert textField.exists();

        textField = uiDevice.findObject(new UiSelector().text("Track my geolocation for event verification"));
        assert textField.exists();

        // Get the bounds of the text "verification"
        android.graphics.Rect bounds = null;
        try {
            bounds = textField.getBounds();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Calculate the center position below the text
        int centerX = bounds.right + 100;
        int centerY = bounds.bottom;

        // Click on the switch at the calculated position
        uiDevice.click(centerX, centerY);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        clickOn("While using the app");

        textField = uiDevice.findObject(new UiSelector().text("Not currently tracking"));
        assert !textField.exists();
    }

    // Organizer
    @Test
    public void test03_02_01_02() {
        goToOrganize();

        // Find the text "verification"
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject textField = uiDevice.findObject(new UiSelector().text("Create Event"));
        assert textField.exists();
        try {
            textField.click();
        } catch (UiObjectNotFoundException e) {
            throw new RuntimeException(e);
        }

        textField = uiDevice.findObject(new UiSelector().text("Track Check-in Geolocation"));
        assert textField.exists();
    }
}
