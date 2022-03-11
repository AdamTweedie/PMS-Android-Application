package com.deitel.pms.supervisor;
import com.deitel.pms.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SupervisorActivity extends AppCompatActivity {

    // TODO - add functionality to the nav bar buttons so they change color on click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervisor_acitivity_navbar);

        final Button btnWorkspace = findViewById(R.id.btnSupervisorWorkspace);
        final Button btnNotifications = findViewById(R.id.btnSupervisorNotifications);
        final Button btnMessages = findViewById(R.id.btnSupervisorMessages);
        final Button btnProfile = findViewById(R.id.btnSupervisorProfile);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fm.getFragments().isEmpty()) {
            fragmentTransaction.add(R.id.supervisor_nav_bar_fragment, new SupervisorWorkspace()).commit();
        }

        btnWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());
                fm.beginTransaction()
                        .add(R.id.supervisor_nav_bar_fragment, new SupervisorWorkspace())
                        .addToBackStack("workspace")
                        .commit();
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());
            }
        });

        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());
                fm.beginTransaction()
                        .add(R.id.supervisor_nav_bar_fragment, new SupervisorProfile())
                        .addToBackStack("supervisor profile")
                        .commit();
            }
        });
    }

    public void clearBackStack(int stackCount) {
        for (int i = 0; i < stackCount; ++i) {
            getSupportFragmentManager().popBackStack();
        }
    }
}