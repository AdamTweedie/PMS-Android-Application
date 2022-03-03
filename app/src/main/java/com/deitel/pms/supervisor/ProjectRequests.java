package com.deitel.pms.supervisor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.ButtonUtils;
import com.deitel.pms.R;


import java.util.ArrayList;

public class ProjectRequests extends Fragment implements ProjectRequestsRecyclerViewAdapter.ItemClickListener{

    ProjectRequestsRecyclerViewAdapter adapter;

    ArrayList<ArrayList<String>> supervisorRecommendedProjectRequests;
    ArrayList<ArrayList<String>> studentSuggestedProjectRequests;

    public ProjectRequests(ArrayList<ArrayList<String>> recommendedProjects,
                           ArrayList<ArrayList<String>> suggestedProjects) {
        this.supervisorRecommendedProjectRequests = recommendedProjects;
        this.studentSuggestedProjectRequests = suggestedProjects;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_project_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button viewSupervisorRecommended = (Button) view.findViewById(R.id.btnSupervisorRecommended);
        Button viewStudentSuggested = (Button) view.findViewById(R.id.btnStudentSuggested);
        adapter = new ProjectRequestsRecyclerViewAdapter(getContext(), null);

        RecyclerView recyclerView = view.findViewById(R.id.rvProjectRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (adapter.getmData()==null) {
            adapter.setmData(getSupervisorRecommendedProjects());
            adapter.setClickListener(ProjectRequests.this);
            recyclerView.setAdapter(adapter);
        }

        viewSupervisorRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonUtils.tabViewButtonColorChanger(viewStudentSuggested, viewSupervisorRecommended);
                adapter.setmData(getSupervisorRecommendedProjects());
                adapter.setClickListener(ProjectRequests.this);
                recyclerView.setAdapter(adapter);
            }
        });

        viewStudentSuggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonUtils.tabViewButtonColorChanger(viewSupervisorRecommended, viewStudentSuggested);
                adapter.setmData(getStudentRecommendedProjects());
                adapter.setClickListener(ProjectRequests.this);
                recyclerView.setAdapter(adapter);
            }
        });
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

    private ArrayList<ArrayList<String>> getSupervisorRecommendedProjects() {
        return this.supervisorRecommendedProjectRequests;
    }

    private ArrayList<ArrayList<String>> getStudentRecommendedProjects() {
        return this.studentSuggestedProjectRequests;
    }
}
