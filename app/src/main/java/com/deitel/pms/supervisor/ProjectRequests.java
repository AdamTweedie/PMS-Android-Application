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
    boolean flag;

    public ProjectRequests(ArrayList<ArrayList<String>> recommendedProjects,
                           ArrayList<ArrayList<String>> suggestedProjects) {
        this.supervisorRecommendedProjectRequests = recommendedProjects;
        this.studentSuggestedProjectRequests = suggestedProjects;
        this.flag = true;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        viewSupervisorRecommended.setOnClickListener(view12 -> {
            ButtonUtils.tabViewButtonColorChanger(viewStudentSuggested, viewSupervisorRecommended);
            adapter.setmData(getSupervisorRecommendedProjects());
            adapter.setClickListener(ProjectRequests.this);
            recyclerView.setAdapter(adapter);
            setFlag(true);
        });

        viewStudentSuggested.setOnClickListener(view1 -> {
            ButtonUtils.tabViewButtonColorChanger(viewSupervisorRecommended, viewStudentSuggested);
            adapter.setmData(getStudentRecommendedProjects());
            adapter.setClickListener(ProjectRequests.this);
            recyclerView.setAdapter(adapter);
            setFlag(false);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        ArrayList<String> projectRequestData = adapter.getItem(position);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.supervisor_nav_bar_fragment,
                        new ExpandedProjectRequest(projectRequestData,
                                ProjectRequests.this, position))
                .addToBackStack("workspace")
                .commit();
    }

    public void removeProjectRequestFromAdapter(int index) {
        if (getFlag()) {
            this.supervisorRecommendedProjectRequests.remove(index);
        } else {
            this.studentSuggestedProjectRequests.remove(index);
        }
        adapter.notifyItemRemoved(index);
    }

    private ArrayList<ArrayList<String>> getSupervisorRecommendedProjects() {
        return this.supervisorRecommendedProjectRequests;
    }

    private ArrayList<ArrayList<String>> getStudentRecommendedProjects() {
        return this.studentSuggestedProjectRequests;
    }

    private void setFlag(boolean b) {
        this.flag = b;
    }

    private boolean getFlag() {
        return this.flag;
    }
}
