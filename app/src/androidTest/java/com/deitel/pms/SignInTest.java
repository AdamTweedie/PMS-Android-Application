package com.deitel.pms;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Test;

public class SignInTest {


    @Test
    public void studentSignInTest() {
        FragmentScenario.launchInContainer(SignIn.class);
        onView(withId(R.id.sign_in)).check(matches(isDisplayed()));
        onView(withId(R.id.etUserEmail)).perform(click(),
                typeText("demo@exeter.ac.uk"), closeSoftKeyboard());
        onView(withId(R.id.etUserPassword)).perform(click(),
                typeText("demo"), closeSoftKeyboard());
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.activity_home_navbar)).check(matches(isDisplayed()));
    }

    @Test
    public void supervisorSignInTest() {
        FragmentScenario.launchInContainer(SignIn.class);
        onView(withId(R.id.etUserEmail)).perform(click(), typeText("supervisor5@exeter.ac.uk"), closeSoftKeyboard());
        onView(withId(R.id.etUserPassword)).perform(click(), typeText("supervisor5"), closeSoftKeyboard());
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.supervisor_activity_navbar)).check(matches(isDisplayed()));
    }

    @Test
    public void incorrectDetailsSignInTest() {
        FragmentScenario.launchInContainer(SignIn.class);
        onView(withId(R.id.etUserEmail)).perform(click(), typeText("errortest@ex.ac.uk"), closeSoftKeyboard());
        onView(withId(R.id.etUserPassword)).perform(click(), typeText("errorsarelame"), closeSoftKeyboard());
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.sign_in)).check(matches(isDisplayed()));
    }
}