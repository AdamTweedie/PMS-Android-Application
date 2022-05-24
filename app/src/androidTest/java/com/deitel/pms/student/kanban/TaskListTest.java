package com.deitel.pms.student.kanban;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.fragment.app.testing.FragmentScenario;

import com.deitel.pms.R;

import org.junit.Test;

public class TaskListTest {

    @Test
    public void onViewCreated() {
        FragmentScenario.launchInContainer(TaskList.class);
        onView(withId(R.id.task_recycler_view)).check(matches(isDisplayed()));
    }
}