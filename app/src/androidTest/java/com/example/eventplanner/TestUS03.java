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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestUS03 extends AbstractTest {
    @Rule
    public ActivityScenarioRule<SplashScreen> scenario = new ActivityScenarioRule<SplashScreen>(SplashScreen.class);
    // Temporary test to make this class not give an error when running without other tests
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.eventplanner", appContext.getPackageName());
    }

    // Tests for US 03.02.01
    // As a user, I want the option to enable or disable geolocation tracking for event verification.
    /*
    @Test
    public void test03_02_01() {
        splashScreenContinue();
    }
     */
}
