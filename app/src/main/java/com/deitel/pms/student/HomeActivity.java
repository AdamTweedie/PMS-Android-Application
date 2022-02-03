package com.deitel.pms.student;
import com.deitel.pms.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    // TODO - add functionality to the nav bar buttons so they change color on click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navbar);

        final Button btnWorkspace = findViewById(R.id.btnNavWorkspace);
        final Button btnNotifications = findViewById(R.id.btnNavNotifications);
        final Button btnSupervisor = findViewById(R.id.btnNavSupervisor);
        final Button btnProfile = findViewById(R.id.btnNavProfile);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fm.getFragments().isEmpty()) {
            fragmentTransaction.add(R.id.nav_bar_fragment, new Workspace()).commit();
        }


        btnWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
