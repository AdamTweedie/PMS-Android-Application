package com.deitel.pms.supervisor;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import com.deitel.pms.MyMatcher;
import com.deitel.pms.R;
import com.google.common.collect.MultimapBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class SupervisorActivityTest {

    // TODO - test for NavToSupervisorNotifications
    @Test
    public void onCreate() {
        ActivityScenario.launch(SupervisorActivity.class);
        onView(MyMatcher.withIndex(withId(R.id.supervisor_workspace), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void navToSupervisorWorkspace() {
        ActivityScenario.launch(SupervisorActivity.class);
        onView(withId(R.id.btnSupervisorWorkspace)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.supervisor_workspace), 1)).check(matches(isDisplayed()));
    }

    @Test
    public void navToSupervisorProfile() {
        ActivityScenario.launch(SupervisorActivity.class);
        onView(withId(R.id.btnSupervisorProfile)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.supervisor_profile), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void navToSupervisorMessages() {
        ActivityScenario.launch(SupervisorActivity.class);
        onView(withId(R.id.btnSupervisorMessages)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.supervisor_messages), 0)).check(matches(isDisplayed()));
    }

}