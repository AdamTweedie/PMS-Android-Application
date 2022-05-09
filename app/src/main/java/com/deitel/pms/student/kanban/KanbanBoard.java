package com.deitel.pms.student.kanban;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.student.Workspace;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KanbanBoard extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_kanban, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final User user = new User();
        final String userId = user.getUserId(requireActivity());
        final String user_kanban_prefs_id = userId+"_kanban_prefs";

        ImageButton addTask = (ImageButton) view.findViewById(R.id.btnAddNewCard);
        ImageButton popFragment = (ImageButton) view.findViewById(R.id.popKanbanFragment);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        TaskList kanbanLists;

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        TabLayout.Tab firstTab = tabLayout.newTab();
        TabLayout.Tab secondTab = tabLayout.newTab();
        TabLayout.Tab thirdTab = tabLayout.newTab();

        firstTab.setText("To-Do");
        secondTab.setText("Doing");
        thirdTab.setText("Done");

        tabLayout.addTab(firstTab, 0, true);
        kanbanLists = new TaskList(getDataFromSharedPrefs(0, requireActivity(),
                user_kanban_prefs_id), 0, user_kanban_prefs_id);
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
                switch(tab.getPosition()) {
                    case 0:
                        kanbanLists.clearAdapter();
                        kanbanLists.fillAdapter(getDataFromSharedPrefs(0,
                                requireActivity(), user_kanban_prefs_id));
                        kanbanLists.setTabPosition(0);
                        break;
                    case 1:
                        kanbanLists.clearAdapter();
                        kanbanLists.fillAdapter(getDataFromSharedPrefs(1,
                                requireActivity(), user_kanban_prefs_id));
                        kanbanLists.setTabPosition(1);
                        break;
                    case 2:
                        kanbanLists.clearAdapter();
                        kanbanLists.fillAdapter(getDataFromSharedPrefs(2,
                                requireActivity(), user_kanban_prefs_id));
                        kanbanLists.setTabPosition(2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
                System.out.println("tab " + tab.getPosition() +
                        " data: " +  kanbanLists.getTaskListData());
                kanbanLists.saveToSharedPrefs(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
            }
        });


        addTask.setOnClickListener(view1 -> {
            String emptyTask = Task.newEmptyTask();
            kanbanLists.addNewTask(emptyTask);
        });

        popFragment.setOnClickListener( view2 -> {
            Fragment fragment = getParentFragmentManager()
                    .findFragmentById(R.id.nav_bar_fragment);

            kanbanLists.saveToSharedPrefs(kanbanLists.getTabPosition());

            if (fragment != null) {
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().beginTransaction()
                        .add(R.id.nav_bar_fragment, new Workspace())
                        .addToBackStack("workspace")
                        .commit();
            }
        });
    }

    public ArrayList<String> getDataFromSharedPrefs(int tabPosition, Activity activity,
                                                    String user_kanban_prefs_id) {
        ArrayList<String> dataHolder = new ArrayList<>();
        String key;
        SharedPreferences sharedPreferences = (SharedPreferences) activity
                .getSharedPreferences(user_kanban_prefs_id, Context.MODE_PRIVATE);
        Set<String> emptyStr = new HashSet<>();

        switch (tabPosition) {
            case 0:
                key = "KANBAN-0";
                dataHolder.addAll(sharedPreferences.getStringSet(key, emptyStr));
                return dataHolder;
            case 1:
                key = "KANBAN-1";
                dataHolder.addAll(sharedPreferences.getStringSet(key, emptyStr));
                return dataHolder;
            case 2:
                key = "KANBAN-2";
                dataHolder.addAll(sharedPreferences.getStringSet(key, emptyStr));
                return dataHolder;
            default:
                return null;
        }
    }
}
