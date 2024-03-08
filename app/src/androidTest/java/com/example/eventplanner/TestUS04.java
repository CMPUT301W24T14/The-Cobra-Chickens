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
public class TestUS04 extends AbstractTest {
    @Rule
    public ActivityScenarioRule<SplashScreen> scenario = new ActivityScenarioRule<SplashScreen>(SplashScreen.class);
    // Temporary test to make this class not give an error when running without other tests
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.eventplanner", appContext.getPackageName());
    }

    // Tests for US 04.01.01
    // As an administrator, I want to be able to remove events.
    /*
    @Test
    public void test04_01_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 04.02.01
    // As an administrator, I want to be able to remove profiles.
    /*
    @Test
    public void test04_02_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 04.03.01
    // As an administrator, I want to be able to remove images.
    /*
    @Test
    public void test04_03_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 04.04.01
    // As an administrator, I want to be able to browse events.
    /*
    @Test
    public void test04_04_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 04.05.01
    // As an administrator, I want to be able to browse profiles.
    /*
    @Test
    public void test004_05_01() {
        splashScreenContinue();
    }
     */

    // Tests for US 04.06.01
    // As an administrator, I want to be able to browse images.
    /*
    @Test
    public void test04_06_01() {
        splashScreenContinue();
    }
     */
}
