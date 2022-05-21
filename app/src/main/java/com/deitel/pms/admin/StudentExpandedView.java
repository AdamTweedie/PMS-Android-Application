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
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.deitel.pms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

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

        ImageButton collapseStudentRequest = view.findViewById(R.id.btnCollapseExpandedStudent);
        Button deleteStudentAccount = view.findViewById(R.id.adminDeleteStudentAccount);
        Button removeStudentSupervisor = view.findViewById(R.id.adminRemoveStudentSupervisor);

        studentEmail.setText(this.email);
        if (title == null) {
            projectTitle.setText("Student yet to choose a project");
        } else {
            projectTitle.setText(this.title);
        }
        if (supervisor == null) {
            supervisorEmail.setText("Student does not yet have a supervisor");
        } else {
            supervisorEmail.setText(this.supervisor);
        }
        if (!approved) {
            removeStudentSupervisor.setVisibility(View.GONE);
            removeStudentSupervisor.setClickable(false);
            projectApproved.setText(this.approved.toString());
        } else {
            projectApproved.setText(this.approved.toString());
        }

        if (removeStudentSupervisor.isClickable()) {
            removeStudentSupervisor.setOnClickListener(view1 -> {
                removeSupervisorFromStudent();
            });
        }

        if (collapseStudentRequest.isClickable()) {
            collapseStudentRequest.setOnClickListener(view1 ->
                    requireActivity().getSupportFragmentManager().popBackStack());
        }

        deleteStudentAccount.setOnClickListener(view2 -> {
            // enable confirm delete
            view.findViewById(R.id.adminAreYouSureToDelete).setVisibility(View.VISIBLE);
            view.findViewById(R.id.adminAreYouSureToDelete).setClickable(true);
            // disable all other functionality
            deleteStudentAccount.setVisibility(View.GONE);
            deleteStudentAccount.setClickable(false);
            removeStudentSupervisor.setVisibility(View.GONE);
            removeStudentSupervisor.setClickable(false);
            collapseStudentRequest.setClickable(false);

            view.findViewById(R.id.adminCancelDelete).setOnClickListener(view4 -> {
                // disable confirm delete
                view.findViewById(R.id.adminAreYouSureToDelete).setVisibility(View.GONE);
                view.findViewById(R.id.adminAreYouSureToDelete).setClickable(false);
                // enable all other activity
                deleteStudentAccount.setVisibility(View.VISIBLE);
                deleteStudentAccount.setClickable(true);
                removeStudentSupervisor.setVisibility(View.VISIBLE);
                removeStudentSupervisor.setClickable(true);
                collapseStudentRequest.setClickable(true);
            });

            view.findViewById(R.id.adminConfirmDelete).setOnClickListener(view3 -> {
                deleteStudentAccount();
                removeStudentFromSupervisor();
                deleteSupervisorStudentMessages();
            });
        });
    }

    private void removeSupervisorFromStudent() {
        // alter students account
        Map<String, Object> noLongerApproved = new HashMap<>();
        noLongerApproved.put("project approved", false);
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(this.email)
                .update(noLongerApproved)
                .addOnSuccessListener(unused -> {
                    Log.w("LOGGER", "Successfully marked project approval as false");
                    removeStudentFromSupervisor();
                })
                .addOnFailureListener(e -> Log.e("LOGGER",
                        "failed to mark project approved as false"));
    }

    private void removeStudentFromSupervisor() {
        // TODO - this wont work if the students does have a supervisor so fix it above before this is called
        // alter supervisors account
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

    private void deleteStudentAccount() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(this.email)
                .delete()
                .addOnSuccessListener(unused -> Log.w("LOGGER", "successfully deleted students account"))
                .addOnFailureListener(e -> Log.e("LOGGER", "failed to delete student account"));
    }

    private void deleteSupervisorStudentMessages() {
        FirebaseFirestore.getInstance()
                .collection("supervisors")
                .document(this.supervisor)
                .collection(this.email+" "+"messages")
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot = task.getResult();
                    ArrayList<String> idList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot) {
                        idList.add(documentSnapshot.getId());
                    }
                    for (String id : idList) {
                        FirebaseFirestore.getInstance()
                                .collection("supervisors")
                                .document(getSupervisor())
                                .collection(getEmail()+" "+"messages")
                                .document(id)
                                .delete()
                                .addOnSuccessListener(unused -> Log.w("LOGGER", "successfully deleted document"))
                                .addOnFailureListener(e -> Log.e("LOGGER", "failed to delete document"));
                    }
                }).addOnFailureListener(e -> Log.e("LOGGER", "Failed to access notification collection"));
    }

    private String getSupervisor() {
        return this.supervisor;
    }

    private String getEmail() {
        return this.email;
    }
}
