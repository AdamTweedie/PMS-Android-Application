package com.deitel.pms.admin;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deitel.pms.R;
import com.deitel.pms.User;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity);

        Button navWorkspace = findViewById(R.id.btnNavAdminWorkspace);
        Button navProfile = findViewById(R.id.btnNavAdminProfile);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fm.getFragments().isEmpty()) {
            fragmentTransaction.add(R.id.admin_nav_bar_fragment, new AdminWorkspace()).commit();
        }

        navWorkspace.setOnClickListener(view -> {
            clearBackStack(fm.getBackStackEntryCount());
            fm.beginTransaction()
                    .add(R.id.admin_nav_bar_fragment, new AdminWorkspace())
                    .addToBackStack("Admin-Workspace")
                    .commit();
        });

        navProfile.setOnClickListener(view1 -> {
            clearBackStack(fm.getBackStackEntryCount());
            fm.beginTransaction()
                    .add(R.id.admin_nav_bar_fragment, new AdminProfile())
                    .addToBackStack("Admin-Profile")
                    .commit();
        });
    }

    private void clearBackStack(int stackCount) {
        for (int i = 0; i < stackCount; ++i) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
