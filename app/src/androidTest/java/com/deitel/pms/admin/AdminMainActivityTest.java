package com.deitel.pms.admin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import com.deitel.pms.R;

import org.junit.Test;

public class AdminMainActivityTest {

    @Test
    public void onCreate() {
        ActivityScenario.launch(AdminMainActivity.class);
        onView(withId(R.id.admin_workspace_fragment_id)).check(matches(isDisplayed()));
    }

}