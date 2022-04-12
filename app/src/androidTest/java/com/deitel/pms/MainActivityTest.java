package com.deitel.pms;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void onCreate() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.sign_in)).check(matches(isDisplayed()));
    }
}