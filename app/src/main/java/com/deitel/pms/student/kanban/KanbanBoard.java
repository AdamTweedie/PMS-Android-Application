package com.deitel.pms.student.kanban;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.deitel.pms.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Map;

public class KanbanBoard extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_kanban, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton addTask = (ImageButton) view.findViewById(R.id.btnAddNewCard);
        ImageButton popFragment = (ImageButton) view.findViewById(R.id.popKanbanFragment);
        Task task = new Task();

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        TaskList kanbanLists = new TaskList(getDataFromSharedPrefs(0));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        TabLayout.Tab firstTab = tabLayout.newTab();
        TabLayout.Tab secondTab = tabLayout.newTab();
        TabLayout.Tab thirdTab = tabLayout.newTab();

        firstTab.setText("To-Do");
        secondTab.setText("Doing");
        thirdTab.setText("Done");

        tabLayout.addTab(firstTab, 0, true);
        ft.add(R.id.view_pager, kanbanLists).commit();
        tabLayout.addTab(secondTab,1);
        tabLayout.addTab(thirdTab, 2);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); // set gravity for tab layout
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#6247AA")); // set the red color for the selected tab indicator.


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                // get the current selected tab's position and replace the fragment accordingly
                System.out.println("YOU MADE IT 1");
                switch(tab.getPosition()) {
                    case 0:
                        kanbanLists.setTaskListData(getDataFromSharedPrefs(0));
                        break;
                    case 1:
                        kanbanLists.setTaskListData(getDataFromSharedPrefs(1));
                        break;
                    case 2:
                        kanbanLists.setTaskListData(getDataFromSharedPrefs(2));
                        break;
                }
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


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> emptyTask = task.newEmptyTask();
                kanbanLists.addNewTask(emptyTask);
            }
        });
    }

    private ArrayList<ArrayList<String>> getDataFromSharedPrefs(int tabPosition) {
        ArrayList<ArrayList<String>> dataHolder = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();

        switch (tabPosition) {
            case 0:
                data.add("hello mite");
                dataHolder.add(data);
                return dataHolder;
            case 1:
                data.add("hyes");
                dataHolder.add(data);
                return dataHolder;
            case 2:
                data.add("hno");
                dataHolder.add(data);
                return dataHolder;
            default:
                return null;
        }
    }
}
