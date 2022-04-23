package com.deitel.pms;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpTest {

    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    @Test
    public void studentSignUpTest() {
        deleteDocument();
        FragmentScenario.launchInContainer(SignUp.class);
        onView(withId(R.id.sign_up)).check(matches(isDisplayed()));
        onView(withId(R.id.etNewEmail)).perform(click(),
                typeText("testyy@exeter.ac.uk"), closeSoftKeyboard());
        onView(withId(R.id.etNewPassword)).perform(click(),
                typeText("testyy"), closeSoftKeyboard());
        onView(withId(R.id.etNewConfirmPassword)).perform(click(),
                typeText("testyy"), closeSoftKeyboard());
        onView(withId(R.id.etUniversityAccessCode)).perform(click(),
                typeText("12345"), closeSoftKeyboard());
        onView(withId(R.id.btnCreateAccount)).perform(click());
        // wait 5 seconds for view
        onView(isRoot()).perform(Waiter.waitId(R.id.activity_home_navbar,
                TimeUnit.SECONDS.toMillis(7)));
        onView(withId(R.id.activity_home_navbar)).check(matches(isDisplayed()));
        deleteDocument();
    }

    @Test
    public void SupervisorSignUpTest() {
        setAccountCreatedToFalse();
        FragmentScenario.launchInContainer(SignUp.class);
        onView(withId(R.id.sign_up)).check(matches(isDisplayed()));
        onView(withId(R.id.etNewEmail)).perform(click(), typeText("testsupervisor@exeter.ac.uk"), closeSoftKeyboard());
        onView(withId(R.id.etNewPassword)).perform(click(), typeText("testsupervisor"), closeSoftKeyboard());
        onView(withId(R.id.etNewConfirmPassword)).perform(click(), typeText("testsupervisor"), closeSoftKeyboard());
        onView(withId(R.id.etUniversityAccessCode)).perform(click(), typeText("123450000"), closeSoftKeyboard());
        onView(withId(R.id.btnCreateAccount)).perform(click());
        // wait 5 seconds for view
        onView(isRoot()).perform(Waiter.waitId(R.id.supervisor_activity_navbar, TimeUnit.SECONDS.toMillis(7)));
        onView(withId(R.id.supervisor_activity_navbar)).check(matches(isDisplayed()));
    }

    private void deleteDocument() {
        dbInstance.collection("users")
                .document("testyy@exeter.ac.uk")
                .delete()
                .addOnSuccessListener(unused -> Log.w("TEST LOG:", "successfully deleted test document"))
                .addOnFailureListener(e -> Log.w("TEST LOG:", "failed to delete test document " + e));
    }

    private void setAccountCreatedToFalse() {
        Map<String, Object> d = new HashMap<>();
        d.put("account created", false);
        dbInstance.collection("supervisors")
                .document("testsupervisor@exeter.ac.uk")
                .set(d)
                .addOnSuccessListener(unused -> Log.w("TEST LOG: ", "successfully set to false"))
                .addOnFailureListener(e -> Log.w("TEST LOG: ", "failed to set to false " + e));
    }
}