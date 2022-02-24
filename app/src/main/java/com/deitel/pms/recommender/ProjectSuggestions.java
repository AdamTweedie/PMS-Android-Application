package com.deitel.pms.recommender;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.deitel.pms.R;
import com.deitel.pms.student.subReferences;

import java.util.ArrayList;
import java.util.Objects;

public class ProjectSuggestions extends Fragment {

    ArrayList<String> project1;
    ArrayList<String> project2;
    ArrayList<String> project3;
    ArrayList<String> project4;

    ProjectSuggestions(ArrayList<String> p1,
                       ArrayList<String> p2,
                       ArrayList<String> p3,
                       ArrayList<String> p4) {

        this.project1 = p1;
        this.project2 = p2;
        this.project3 = p3;
        this.project4 = p4;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_recommended_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getParentFragmentManager();

        ImageButton btnSelectProject1 = view.findViewById(R.id.btnBubble1);
        ImageButton btnSelectProject2 = view.findViewById(R.id.btnBubble2);
        ImageButton btnSelectProject3 = view.findViewById(R.id.btnBubble3);
        ImageButton btnSelectProject4 = view.findViewById(R.id.btnBubble4);

        TextView title1 = view.findViewById(R.id.tvTitle1);
        TextView title2 = view.findViewById(R.id.tvTitle2);
        TextView title3 = view.findViewById(R.id.tvTitle3);
        TextView title4 = view.findViewById(R.id.tvTitle4);

        title1.setText(getProjectTitle(1));
        title2.setText(getProjectTitle(2));
        title3.setText(getProjectTitle(3));
        title4.setText(getProjectTitle(4));

        btnSelectProject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFragment(fm, 1);
            }
        });

        btnSelectProject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFragment(fm, 2);
            }
        });

        btnSelectProject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFragment(fm, 3);
            }
        });

        btnSelectProject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFragment(fm, 4);
            }
        });
    }

    public String getProjectTitle(int projectId) {
        switch (projectId){
            case 1:
                return this.project1.get(2);
            case 2:
                return this.project2.get(2);
            case 3:
                return this.project3.get(2);
            case 4:
                return this.project4.get(2);
            default:
                return null;
        }
    }

    private String getProjectDescription(int projectId) {
        switch (projectId){
            case 1:
                return this.project1.get(3);
            case 2:
                return this.project2.get(3);
            case 3:
                return this.project3.get(3);
            case 4:
                return this.project4.get(3);
            default:
                return null;
        }
    }

    private String getSupervisorEmail(int projectId) {
        switch (projectId){
            case 1:
                return this.project1.get(0);
            case 2:
                return this.project2.get(0);
            case 3:
                return this.project3.get(0);
            case 4:
                return this.project4.get(0);
            default:
                return null;
        }
    }

    private String getSupervisorName(int projectId) {
        switch (projectId){
            case 1:
                return this.project1.get(1);
            case 2:
                return this.project2.get(1);
            case 3:
                return this.project3.get(1);
            case 4:
                return this.project4.get(1);
            default:
                return null;
        }
    }

    public void createFragment(FragmentManager fragmentManager, int projectId) {
        fragmentManager.beginTransaction()
                .add(R.id.recommenderContainterView, new SelectedProject(getProjectTitle(projectId),
                        getProjectDescription(projectId),
                        getSupervisorName(projectId),
                        getSupervisorEmail(projectId))).addToBackStack("project").commit();
    }
}
