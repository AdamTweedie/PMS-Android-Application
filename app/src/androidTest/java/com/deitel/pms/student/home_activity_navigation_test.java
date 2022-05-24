package com.deitel.pms.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import com.deitel.pms.MyMatcher;
import com.deitel.pms.R;

import org.junit.Test;

public class home_activity_navigation_test {

    @Test
    public void homeActivityToFragmentNavigationTest() {

        ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.btnCalendarView)).perform(click());
        onView(withId(R.id.student_calendar_parent)).check(matches(isDisplayed()));
        onView(withId(R.id.btnGoBack)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.frag_workspace_layout_parent),
                0)).check(matches(isDisplayed()));
        onView(withId(R.id.btnReferencingGuide)).perform(click());
        onView(withId(R.id.references_parent)).check(matches(isDisplayed()));
        onView(withId(R.id.refBtnGoBack)).perform(click());
        onView(MyMatcher.withIndex(withId(R.id.frag_workspace_layout_parent),
                0)).check(matches(isDisplayed()));
        onView(withId(R.id.btnKanbanBoard)).perform(click());
        onView(withId(R.id.kanbanBoardParent)).check(matches(isDisplayed()));
        // Navigate
        onView(withId(R.id.popKanbanFragment)).perform(click());
        // Verify
        onView(MyMatcher.withIndex(withId(R.id.frag_workspace_layout_parent),
                0)).check(matches(isDisplayed()));
        // Navigate
        onView(MyMatcher.withIndex(withId(R.id.btnMyProject), 0)).perform(click());
        // verify
        //onView(withId(R.id.myProjectParent)).check(matches(isDisplayed()));
        // Navigate
        onView(withId(R.id.btnPopMyProjectFragment)).perform(click());
        // Verify
        onView(MyMatcher.withIndex(withId(R.id.frag_workspace_layout_parent),
                0)).check(matches(isDisplayed()));

        // Navigate
        onView(withId(R.id.btnNavNotifications)).perform(click());
        // Verify
        onView(withId(R.id.notifications_parent)).check(matches(isDisplayed()));
        // Navigate
        onView(withId(R.id.btnNavProfile)).perform(click());
        // verify
        onView(withId(R.id.profile_parent)).check(matches(isDisplayed()));
    }
}
