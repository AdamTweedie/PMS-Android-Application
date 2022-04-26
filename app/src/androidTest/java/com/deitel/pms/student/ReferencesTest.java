package com.deitel.pms.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import com.deitel.pms.MyMatcher;
import com.deitel.pms.R;

import org.junit.Test;

public class ReferencesTest {

    @Test
    public void onViewCreated() {
        FragmentScenario.launchInContainer(References.class);
        onView(withId(R.id.references_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void references_styleAPA() {
        FragmentScenario.launchInContainer(References.class);
        onView(withId(R.id.refAPA)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.refOxford)).check(matches(isNotChecked()));
        onView(withId(R.id.refACM)).check(matches(isNotChecked()));
        onView(withId(R.id.refHarvard)).check(matches(isNotChecked()));
        onView(withId(R.id.tvRefStyle)).check(matches(withText("APA")));
    }

    @Test
    public void references_styleOxford() {
        FragmentScenario.launchInContainer(References.class);
        onView(withId(R.id.refOxford)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.refAPA)).check(matches(isNotChecked()));
        onView(withId(R.id.refACM)).check(matches(isNotChecked()));
        onView(withId(R.id.refHarvard)).check(matches(isNotChecked()));
        onView(withId(R.id.tvRefStyle)).check(matches(withText("Oxford")));
    }

    @Test
    public void references_styleACM() {
        FragmentScenario.launchInContainer(References.class);
        onView(withId(R.id.refACM)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.refOxford)).check(matches(isNotChecked()));
        onView(withId(R.id.refAPA)).check(matches(isNotChecked()));
        onView(withId(R.id.refHarvard)).check(matches(isNotChecked()));
        onView(withId(R.id.tvRefStyle)).check(matches(withText("ACM")));
    }

    @Test
    public void references_styleHarvard() {
        FragmentScenario.launchInContainer(References.class);
        onView(withId(R.id.refHarvard)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.refOxford)).check(matches(isNotChecked()));
        onView(withId(R.id.refACM)).check(matches(isNotChecked()));
        onView(withId(R.id.refAPA)).check(matches(isNotChecked()));
        onView(withId(R.id.tvRefStyle)).check(matches(withText("Harvard")));
    }
}