package com.deitel.pms.supervisor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;


import java.util.ArrayList;

public class ProjectRequests extends Fragment implements ProjectRequestsRecyclerViewAdapter.ItemClickListener{

    ProjectRequestsRecyclerViewAdapter adapter;

    ArrayList<ArrayList<String>> projectRequests;

    public ProjectRequests(ArrayList<ArrayList<String>> projects) {
        this.projectRequests = projects;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_project_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.rvProjectRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println("Project Requests - " + getProjectRequests());
        adapter = new ProjectRequestsRecyclerViewAdapter(getContext(), getProjectRequests());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked on row number " + position, Toast.LENGTH_SHORT).show();
        ArrayList<String> projectRequestData = adapter.getItem(position);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.supervisor_nav_bar_fragment,
                        new ExpandedProjectRequest(projectRequestData))
                .addToBackStack("workspace")
                .commit();
    }

    private ArrayList<ArrayList<String>> getProjectRequests() {
        return this.projectRequests;
    }
}
