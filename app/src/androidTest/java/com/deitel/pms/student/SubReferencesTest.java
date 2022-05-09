package com.deitel.pms.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.fragment.app.testing.FragmentScenario;

import com.deitel.pms.R;

import org.junit.Test;

public class SubReferencesTest {

    @Test
    public void onViewCreated() {
        FragmentScenario.launchInContainer(SubReferences.class);
        onView(withId(R.id.child_ref_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void getStyle() {
        FragmentScenario.launchInContainer(SubReferences.class);
        SubReferences subReferences = new SubReferences("ACM");


    }
}