package com.deitel.pms.admin;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentExpandedView extends Fragment {

    private final String email;
    private final String title;
    private final String supervisor;
    private final Boolean approved;

    StudentExpandedView(String studentEmail, String projectTitle, String supervisorEmail,
                        Boolean projectApproved) {
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

        ImageButton collapseStudent = view.findViewById(R.id.btnCollapseExpandedStudent);
        Button sendNotification = view.findViewById(R.id.adminSendNotification);
        Button resetProject = view.findViewById(R.id.adminResetStudentProject);

        studentEmail.setText(this.email);
        if (title != null) {
            projectTitle.setText(this.title);
        } else {
            projectTitle.setText("Student yet to choose a project");
        }
        if (supervisor != null) {
            supervisorEmail.setText(this.supervisor);
        } else {
            supervisorEmail.setText("Student does not yet have a supervisor");
        }

        projectApproved.setText(this.approved.toString());

        resetProject.setOnClickListener(view1 -> {
            if (this.supervisor!=null) {
                removeStudentFromSupervisor();
                deleteSupervisorStudentMessages("users", this.email, this.supervisor);
                deleteSupervisorStudentMessages("supervisors", this.supervisor, this.email);
            }
            resetStudentProject();
        } );

        if (collapseStudent.isClickable()) {
            collapseStudent.setOnClickListener(view1 ->
                    requireActivity().getSupportFragmentManager().popBackStack());
        }

        sendNotification.setOnClickListener(view2 -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.admin_nav_bar_fragment, new AdminCreateNotification(this.email, "users"))
                    .addToBackStack("new notification")
                    .commit();
        });
    }

    private void resetStudentProject() {
        // alter students account
        Map<String, Object> updateStudent = new HashMap<>();
        updateStudent.put("approved project", false);
        updateStudent.put("supervisor email", null);
        updateStudent.put("supervisor name", null);
        updateStudent.put("project title", null);
        updateStudent.put("project description", null);
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(this.email)
                .update(updateStudent)
                .addOnSuccessListener(unused -> {
                    Log.w("LOGGER", "Successfully marked project approval as false");
                    removeStudentFromSupervisor();
                })
                .addOnFailureListener(e -> Log.e("LOGGER",
                        "failed to mark project approved as false"));
    }

    private void removeStudentFromSupervisor() {
        FirebaseFirestore.getInstance()
                .collection("supervisors")
                .document(this.supervisor)
                .collection("approved projects")
                .document(this.email)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.w("LOGGER", "Successfully removed student from supervisor");
                    Toast.makeText(getContext(), "Successfully removed supervisor !",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("LOGGER",
                        "Failed to remove student from supervisor"));
    }

    private void deleteSupervisorStudentMessages(String collectionPath, String firstUser, String secondUser) {
        FirebaseFirestore.getInstance()
                .collection(collectionPath)
                .document(firstUser)
                .collection(secondUser+" "+"messages")
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot = task.getResult();
                    ArrayList<String> idList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot) {
                        idList.add(documentSnapshot.getId());
                    }
                    for (String id : idList) {
                        FirebaseFirestore.getInstance()
                                .collection(collectionPath)
                                .document(firstUser)
                                .collection(secondUser+" "+"messages")
                                .document(id)
                                .delete()
                                .addOnSuccessListener(unused -> Log.w("LOGGER", "successfully deleted document"))
                                .addOnFailureListener(e -> Log.e("LOGGER", "failed to delete document"));
                    }
                }).addOnFailureListener(e -> Log.e("LOGGER", "Failed to access notification collection"));
    }
}
