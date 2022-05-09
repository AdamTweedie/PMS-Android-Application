package com.deitel.pms.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import androidx.fragment.app.testing.FragmentScenario;

import com.deitel.pms.R;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkspaceTest {

    @Test
    public void onCreateView() {
    }

    @Test
    public void onViewCreated() {
        FragmentScenario.launchInContainer(Workspace.class);
        onView(withId(R.id.frag_workspace_layout_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void workspace_CalendarDate() {
        FragmentScenario.launchInContainer(Workspace.class);
        DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MMM");
        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();
        onView(withId(R.id.tvCurrentDateDay)).check(matches(withText(dtfDay.format(now))));
        onView(withId(R.id.tvCurrentDateMonth)).check(matches(withText(dtfMonth.format(now))));
    }

}