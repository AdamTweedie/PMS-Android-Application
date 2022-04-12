package com.deitel.pms.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.deitel.pms.R.id.frag_workspace_layout_parent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import com.deitel.pms.MyMatcher;
import com.deitel.pms.R;
import com.deitel.pms.supervisor.SupervisorActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Test
    public void navWorkspaceFragment() {
        ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.btnNavWorkspace)).perform(click());
        onView(MyMatcher.withIndex(withId(frag_workspace_layout_parent), 1)).check(matches(isDisplayed()));
    }

    @Test
    public void navNotificationFragment() {
        ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.btnNavNotifications)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.notifications_parent), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void navProfileFragment() {
        ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.btnNavProfile)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.profile_parent), 0)).check(matches(isDisplayed()));
    }

}

