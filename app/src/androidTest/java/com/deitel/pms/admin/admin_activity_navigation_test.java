package com.deitel.pms.admin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;

import com.deitel.pms.MyMatcher;
import com.deitel.pms.R;
import com.deitel.pms.Waiter;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

public class admin_activity_navigation_test {

    @Test
    public void adminActivityToFragmentNavigationTest() {

        ActivityScenario.launch(AdminMainActivity.class);

        //Navigate
        onView(withId(R.id.btnNavAdminWorkspace)).perform(click());
        //Verify
        onView(MyMatcher.withIndex(withId(R.id.admin_workspace_fragment_id),1)).check(matches(isDisplayed()));

        //Navigate
        onView(MyMatcher.withIndex(withId(R.id.adminBtnViewStudents), 0)).perform(click());
        Waiter.waitId(R.id.admin_workspace_fragment_id, 5000);
        //Verify
        onView(MyMatcher.withIndex(withId(R.id.admin_workspace_fragment_id),1)).check(matches(isDisplayed()));


        //Navigate
        onView(withId(R.id.btnNavAdminWorkspace)).perform(click());
        //Verify
        onView(MyMatcher.withIndex(withId(R.id.admin_workspace_fragment_id),1)).check(matches(isDisplayed()));

        //Navigate
        onView(MyMatcher.withIndex(withId(R.id.adminBtnViewSupervisors), 0)).perform(click());
        Waiter.waitId(R.id.admin_workspace_fragment_id, 5000);
        //Verify
        onView(MyMatcher.withIndex(withId(R.id.admin_workspace_fragment_id),1)).check(matches(isDisplayed()));


        //Navigate
        onView(withId(R.id.btnNavAdminProfile)).perform(click());
        Waiter.waitId(R.id.admin_workspace_fragment_id, 5000);
        //Verify
        onView(withId(R.id.supervisor_profile)).check(matches(isDisplayed()));

    }
}
