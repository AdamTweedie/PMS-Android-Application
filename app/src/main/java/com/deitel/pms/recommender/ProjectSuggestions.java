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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deitel.pms.R;
import com.deitel.pms.student.subReferences;

import java.util.ArrayList;
import java.util.Objects;

public class ProjectSuggestions extends Fragment {

    ArrayList<String> project1;
    ArrayList<String> project2;
    ArrayList<String> project3;
    ArrayList<String> project4;
    ArrayList<String> project5;
    ArrayList<String> project6;

    ProjectSuggestions(ArrayList<String> p1,
                       ArrayList<String> p2,
                       ArrayList<String> p3,
                       ArrayList<String> p4,
                       ArrayList<String> p5,
                       ArrayList<String> p6) {

        this.project1 = p1;
        this.project2 = p2;
        this.project3 = p3;
        this.project4 = p4;
        this.project5 = p5;
        this.project6 = p6;
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
        ImageButton btnSelectProject5 = view.findViewById(R.id.btnBubble5);
        ImageButton btnSelectProject6 = view.findViewById(R.id.btnBubble6);
        ImageButton btnRedoInterests = view.findViewById(R.id.btnResetProjectInterests);

        TextView title1 = view.findViewById(R.id.tvTitle1);
        TextView title2 = view.findViewById(R.id.tvTitle2);
        TextView title3 = view.findViewById(R.id.tvTitle3);
        TextView title4 = view.findViewById(R.id.tvTitle4);
        TextView title5 = view.findViewById(R.id.tvTitle5);
        TextView title6 = view.findViewById(R.id.tvTitle6);

        title1.setText(getProjectTitle(1));
        title2.setText(getProjectTitle(2));
        title3.setText(getProjectTitle(3));
        title4.setText(getProjectTitle(4));
        title5.setText(getProjectTitle(5));
        title6.setText(getProjectTitle(6));

        btnRedoInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.clearFragmentResult("bundleKey");
                fm.popBackStack();
                fm.beginTransaction()
                        .add(R.id.recommenderContainterView, new SubjectInterestSpinner())
                        .addToBackStack("redo interests").commit();
            }
        });

        btnSelectProject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!project1.isEmpty()) {
                    createFragment(fm, 1);
                } else {
                    Toast.makeText(getContext(), "No project !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSelectProject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!project2.isEmpty()) {
                    createFragment(fm, 2);
                } else {
                    Toast.makeText(getContext(), "No project !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectProject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!project3.isEmpty()) {
                    createFragment(fm, 3);
                } else {
                    Toast.makeText(getContext(), "No project !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectProject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!project4.isEmpty()) {
                    createFragment(fm, 4);
                } else {
                    Toast.makeText(getContext(), "No project !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectProject5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!project5.isEmpty()) {
                    createFragment(fm, 5);
                } else {
                    Toast.makeText(getContext(), "No project !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectProject6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!project6.isEmpty()) {
                    createFragment(fm, 6);
                } else {
                    Toast.makeText(getContext(), "No project !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getProjectTitle(int projectId) {
        switch (projectId){
            case 1:
                if (!isEmpty(project1)) {
                    return this.project1.get(2);
                }
            case 2:
                if (!isEmpty(project2)) {
                    return this.project2.get(2);
                }
            case 3:
                if (!isEmpty(project3)) {
                    return this.project3.get(2);
                }
            case 4:
                if (!isEmpty(project4)) {
                    return this.project4.get(2);
                }
            case 5:
                if (!isEmpty(project5)) {
                    return this.project5.get(2);
                }
            case 6:
                if (!isEmpty(project6)) {
                    return this.project6.get(2);
                }
            default:
                return null;
        }
    }

    private String getProjectDescription(int projectId) {
        switch (projectId){
            case 1:
                if (!isEmpty(project1)) {
                    return this.project1.get(3);
                }
            case 2:
                if (!isEmpty(project2)) {
                    return this.project2.get(3);
                }
            case 3:
                if (!isEmpty(project3)) {
                    return this.project3.get(3);
                }
            case 4:
                if (!isEmpty(project4)) {
                    return this.project4.get(3);
                }
            case 5:
                if (!isEmpty(project5)) {
                    return this.project5.get(3);
                }
            case 6:
                if (!isEmpty(project6)) {
                    return this.project6.get(3);
                }
            default:
                return null;
        }
    }

    private String getSupervisorEmail(int projectId) {
        switch (projectId){
            case 1:
                if (!isEmpty(project1)) {
                    return this.project1.get(0);
                }
            case 2:
                if (!isEmpty(project2)) {
                    return this.project2.get(0);
                }
            case 3:
                if (!isEmpty(project3)) {
                    return this.project3.get(0);
                }
            case 4:
                if (!isEmpty(project4)) {
                return this.project4.get(0);
                }
            case 5:
                if (!isEmpty(project5)) {
                    return this.project5.get(0);
                }
            case 6:
                if (!isEmpty(project5)) {
                    return this.project5.get(0);
                }
            default:
                return null;
        }
    }

    private String getSupervisorName(int projectId) {
        switch (projectId){
            case 1:
                if (!isEmpty(this.project1)) {
                    return this.project1.get(1);
                }
            case 2:
                if (!isEmpty(this.project2)) {
                    return this.project2.get(1);
                }
            case 3:
                if (!isEmpty(this.project3)) {
                    return this.project3.get(1);
                }
            case 4:
                if (!isEmpty(this.project4)) {
                    return this.project4.get(1);
                }
            case 5:
                if (!isEmpty(this.project5)) {
                    return this.project5.get(1);
                }
            case 6:
                if (!isEmpty(this.project6)) {
                    return this.project6.get(1);
                }
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

    public Boolean isEmpty(ArrayList<String> arr) {
        return arr.isEmpty();
    }

}
