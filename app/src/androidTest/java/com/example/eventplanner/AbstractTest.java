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

public abstract class AbstractTest {
    public void splashScreenContinue() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
        try {onView(withId(R.id.btn_continue)).perform(click());} catch (Exception ignored) {}
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
    }
}
