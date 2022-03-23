package com.deitel.pms.student.kanban;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskList extends Fragment implements KanbanRecyclerViewAdapter.ItemClickListener {

    KanbanRecyclerViewAdapter adapter;

    ArrayList<ArrayList<String>> taskListData;
    public TaskList(ArrayList<ArrayList<String>> data) {
        this.taskListData = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.kanban_task_list_child_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new KanbanRecyclerViewAdapter(getContext(), this.taskListData);
        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void updateList() {
        // TODO - this
    }

    public void addNewTask(ArrayList<String> task) {
        System.out.println("HERE MITE 007");
        taskListData.add(task);
        adapter.notifyItemInserted(taskListData.size());
    }

    public void setTaskListData(ArrayList<ArrayList<String>> taskListData) {
        this.taskListData = taskListData;
        System.out.println("CURRENT DATA IS - " + this.taskListData);
    }
}
