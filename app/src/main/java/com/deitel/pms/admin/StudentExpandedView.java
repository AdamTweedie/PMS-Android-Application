package com.deitel.pms.admin;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;

public class StudentExpandedView extends Fragment {

    private String email;
    private String title;
    private String supervisor;
    private Boolean approved;

    StudentExpandedView(String studentEmail, String projectTitle, String supervisorEmail, Boolean projectApproved) {
        this.email = studentEmail;
        this.title = projectTitle;
        this.supervisor = supervisorEmail;
        this.approved = projectApproved;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_expanded_student_view, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView studentEmail = view.findViewById(R.id.adminEtStudentEmail);
        TextView projectTitle = view.findViewById(R.id.adminESProjectTitle);
        TextView supervisorEmail = view.findViewById(R.id.adminESSupervisorName);
        TextView projectApproved = view.findViewById(R.id.adminESProjectApproved);

        studentEmail.setText(this.email);
        projectTitle.setText(this.title);
        supervisorEmail.setText(this.supervisor);
        projectApproved.setText(this.approved.toString());
    }
}
