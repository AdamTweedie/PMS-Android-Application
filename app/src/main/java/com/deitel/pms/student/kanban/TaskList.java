package com.deitel.pms.student.kanban;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TaskList extends Fragment implements KanbanRecyclerViewAdapter.ItemClickListener {

    KanbanRecyclerViewAdapter adapter;

    String user_kanban_prefs_id;
    ArrayList<String> taskListData;
    int tabPosition;
    public TaskList(ArrayList<String> data, int position, String sp_key) {
        this.taskListData = data;
        this.tabPosition = position;
        this.user_kanban_prefs_id = sp_key;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.kanban_task_list_child_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new KanbanRecyclerViewAdapter(getContext(), getTaskListData(), this.tabPosition, TaskList.this);
        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println("clicky click click");
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void fillAdapter(ArrayList<String> data) {
        int insertIndex = 0;
        System.out.println("data being filled : " + data);
        this.taskListData.addAll(0, data);
        adapter.notifyItemRangeInserted(insertIndex, data.size());
    }

    public void clearAdapter() {
        int startIndex = 0; // inclusive
        int size = this.taskListData.size(); // exclusive
        this.taskListData.clear();
        adapter.notifyItemRangeRemoved(startIndex, size);
    }

    public void addNewTask(String task) {
        this.taskListData.add(task);
        adapter.notifyItemInserted(taskListData.size());
    }

    public void saveToSharedPrefs(int tabId) {
        SharedPreferences sharedPreferences = requireActivity()
                .getSharedPreferences(this.user_kanban_prefs_id, Context.MODE_PRIVATE);
        final String key = "KANBAN-" + tabId;
        Set<String> set = new HashSet<>(getTaskListData());
        System.out.println("data going into set + " + getTaskListData());
        System.out.println("SET = " + set);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, set);
        editor.commit();
    }

    public void addToPrefs(int tabPosition, String task) {
        SharedPreferences sharedPreferences = requireActivity()
                .getSharedPreferences(this.user_kanban_prefs_id, Context.MODE_PRIVATE);
        final String key = "KANBAN-" + tabPosition;
        KanbanBoard kb = new KanbanBoard();
        ArrayList<String> tabData = kb.getDataFromSharedPrefs(tabPosition, requireActivity(), this.user_kanban_prefs_id);
        tabData.add(task);

        Set<String> set = new HashSet<>(tabData);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, set);
        editor.commit();
    }

    public ArrayList<String> getTaskListData() {
        return this.taskListData;
    }

    public void setTaskListData(ArrayList<String> data) {
        this.taskListData = data;
    }

    public int getTabPosition() {
        return this.tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
        adapter.setTabPosition(tabPosition);
    }
}
