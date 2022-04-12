package com.deitel.pms;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

public class main_activity_navigation_test {

    @Test
    public void mainActivityToFragmentNavigationTest() {
        ActivityScenario.launch(MainActivity.class);

        // Navigate
        onView(withId(R.id.btnSignUp)).perform(click());

        // Verify
        onView(withId(R.id.sign_up)).check(matches(isDisplayed()));

        // Navigate
        onView(withId(R.id.btnBackToSignIn)).perform(click());

        // Verify
        onView(withId(R.id.sign_in)).check(matches(isDisplayed()));
    }
}
