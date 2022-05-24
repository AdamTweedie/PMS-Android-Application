package com.deitel.pms.admin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;

import com.deitel.pms.R;

import org.junit.Test;

import java.util.ArrayList;

public class AdminStudentSupervisorViewTest {

    @Test
    public void onViewCreated() {
        ArrayList<String> users = new ArrayList<>();
        users.add("testy@exeter.ac.uk");
        users.add("adam@exeter.ac.uk");

        AdminStudentSupervisorView adminStudentSupervisorView = new AdminStudentSupervisorView(users, true);
        FragmentScenario.launchInContainer(adminStudentSupervisorView.getClass());
        onView(withId(R.id.notifications_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickToStudent() {
        ArrayList<String> users = new ArrayList<>();
        users.add("testy@exeter.ac.uk");
        users.add("adam@exeter.ac.uk");
    }

    @Test
    public void onClickToSupervisor() {

    }
}