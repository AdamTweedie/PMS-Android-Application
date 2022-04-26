package com.deitel.pms.student;

import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.testing.FragmentScenario;

import com.deitel.pms.MainActivity;
import com.deitel.pms.R;

import org.junit.Test;

import androidx.lifecycle.Lifecycle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

public class ProfileTest {

    // TODO - add tests for email and supervisor email

    @Test
    public void onViewCreated() {
        FragmentScenario.launchInContainer(Profile.class);
        onView(withId(R.id.profile_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void signOut_HomeActivityDestroyed() {
        ActivityScenario<HomeActivity> activityScenario = ActivityScenario.launch(HomeActivity.class);
        FragmentScenario.launchInContainer(Profile.class);
        onView(withId(R.id.btnAccountSignOut)).perform(click());

        assertSame(activityScenario.getState(), Lifecycle.State.DESTROYED);
    }

    @Test
    public void signOut_MainActivityDisplayed() {
        FragmentScenario.launchInContainer(Profile.class);
        onView(withId(R.id.btnAccountSignOut)).perform(click());

        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
    }



    @Test
    public void unsaveUserCredentials() {
        ActivityScenario.launch(MainActivity.class);
        FragmentScenario.launchInContainer(Profile.class);
        onView(withId(R.id.btnAccountSignOut)).perform(click());
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        SharedPreferences preferences = context.getSharedPreferences("save_creds", Context.MODE_PRIVATE);
        String saved = preferences.getString("rememberMe", "defaultValue");
        assert(!saved.equals("yes"));
    }

}