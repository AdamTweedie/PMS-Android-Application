package com.deitel.pms.admin;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;
import com.deitel.pms.supervisor.SupervisorProfile;

public class SupervisorExpandedView extends Fragment {

    private final String supervisorEmail;
    SupervisorExpandedView(String email) {
        this.supervisorEmail = email;
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

        TextView tvGS = view.findViewById(R.id.tv1);
        tvGS.setText("Group Size");
        TextView groupSize = view.findViewById(R.id.adminESProjectTitle);
        SupervisorProfile.setSupervisorGroupCount(groupSize, this.supervisorEmail);

        view.findViewById(R.id.tv2).setVisibility(View.GONE);
        view.findViewById(R.id.adminESSupervisorName).setVisibility(View.GONE);
        view.findViewById(R.id.tv3).setVisibility(View.GONE);
        view.findViewById(R.id.adminESProjectApproved).setVisibility(View.GONE);
        view.findViewById(R.id.adminResetStudentProject).setVisibility(View.GONE);

        TextView supervisorId = view.findViewById(R.id.adminEtStudentEmail);
        supervisorId.setText(this.supervisorEmail);

        view.findViewById(R.id.btnCollapseExpandedStudent).setOnClickListener(view1 ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack());

        Button sendNotification = view.findViewById(R.id.adminSendNotification);
        sendNotification.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.admin_nav_bar_fragment, new AdminCreateNotification(this.supervisorEmail, "supervisors"))
                    .addToBackStack("new notification")
                    .commit();
        });
    }
}
