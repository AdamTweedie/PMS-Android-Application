package com.deitel.pms.student.kanban;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.deitel.pms.R;
import com.google.android.material.tabs.TabLayout;

public class KanbanBoard extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_kanban, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        TabLayout.Tab firstTab = tabLayout.newTab();
        TabLayout.Tab secondTab = tabLayout.newTab();
        TabLayout.Tab thirdTab = tabLayout.newTab();

        firstTab.setText("To-Do");
        secondTab.setText("Doing");
        thirdTab.setText("Done");

        tabLayout.addTab(firstTab, 0,true); // this tab will be automatically selected on load
        tabLayout.addTab(secondTab,1);
        tabLayout.addTab(thirdTab, 2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); // set gravity for tab layout
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#6247AA")); // set the red color for the selected tab indicator.

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                // get the current selected tab's position and replace the fragment accordingly
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment todoFragment, doingFragment, doneFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        todoFragment = new KanbanListFragment();
                        ft.replace(R.id.view_pager, todoFragment);
                        break;
                    case 1:
                        doingFragment = new KanbanListFragment();
                        ft.replace(R.id.view_pager, doingFragment);
                        break;
                    case 2:
                        doneFragment = new KanbanListFragment();
                        ft.replace(R.id.view_pager, doneFragment);
                }

                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
            }
        });



    }
}
