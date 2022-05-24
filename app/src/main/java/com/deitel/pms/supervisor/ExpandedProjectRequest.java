package com.deitel.pms.supervisor;

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
import androidx.fragment.app.Fragment;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandedProjectRequest extends Fragment {

    final FirestoreUtils utils = new FirestoreUtils();
    final User user = new User();
    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    ArrayList<String> projectData;
    private final String studentId;
    private final String projectTitle;
    private final String projectDescription;
    private final String projectSupervisor;
    private final ProjectRequests list;
    private final int index;

    public ExpandedProjectRequest(ArrayList<String> data, ProjectRequests projectRequests,
                                  int listIndex) {
        this.projectData = data;
        this.studentId = data.get(0);
        this.projectTitle = data.get(1);
        this.projectDescription = data.get(2);
        this.projectSupervisor = data.get(3);
        this.list = projectRequests;
        this.index = listIndex;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expanded_project_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton collapseRequest = (ImageButton) view.findViewById(R.id.btnCollapseExpandedRequest);
        TextView tvStudentId = (TextView) view.findViewById(R.id.expandedReqStudentId);
        TextView tvProjTitle = (TextView) view.findViewById(R.id.expandedReqProjTitle);
        TextView tvProjDescription = (TextView) view.findViewById(R.id.expandedReqProjDescription);
        Button btnAcceptRequest = (Button) view.findViewById(R.id.btnAcceptProjectRequest);
        Button btnDeclineRequest = (Button) view.findViewById(R.id.btnDeclineProjectRequest);

        tvStudentId.setText(this.studentId + " would like you to supervise: ");
        tvProjTitle.setText(this.projectTitle);
        tvProjDescription.setText(this.projectDescription);

        collapseRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set student project approval to true
                Map<String, Object> studentUpdates = new HashMap<>();
                studentUpdates.put(utils.getFIELD_SUPERVISOR_EMAIL(), user.getUserId(requireActivity()));
                studentUpdates.put(utils.getFIELD_PROJECT_APPROVED(), true);

                dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                        .document(getProjectData().get(0)).update(studentUpdates)
                        .addOnSuccessListener(unused -> {
                            Log.w("LOGGER", "successfully updated student project data");

                            // Add student to supervisors myStudents collection
                            Map<String, Object> newMyStudent = new HashMap<>();
                            newMyStudent.put("project title", getProjectData().get(1));
                            newMyStudent.put("project description", getProjectData().get(2));

                            dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                                    .document(user.getUserId(requireActivity()))
                                    .collection("approved projects")
                                    .document(getProjectData().get(0))
                                    .set(newMyStudent)
                                    .addOnCompleteListener(task -> {
                                        // if project request is not attached to specific supervisor
                                        if (getProjectData().get(3)==null) {
                                            // delete student suggested project from requests
                                            deleteUserSuggestedProject();
                                        } else {
                                            // delete supervisor recommended project from requests
                                            deleteSupervisorRecommendedProject();
                                        }
                                        list.removeProjectRequestFromAdapter(getIndex());
                                    }).addOnFailureListener(e -> Log.w("LOGGER", "failed to add student to my projects"));
                            // send notification to student.

                            Toast.makeText(getContext(), "Successfully accepted project request!", Toast.LENGTH_SHORT).show();
                            sendProjectAcceptedNotification(getProjectData().get(0));
                        }).addOnFailureListener(e -> Log.w("LOGGER", "Failed to set project approval to true"));
            }
        });


        if (this.projectSupervisor==null) {
            btnDeclineRequest.setVisibility(View.GONE);
        }
        btnDeclineRequest.setOnClickListener(view1 -> {
            final String title = "Project Request Update!";
            final String description = "You project request has been declined by the original supervisor," +
                    " it has been passed to all other supervisors to review for approval! ";

            // Delete Project Request
            utils.deleteProjectRequest(user.getUserId(requireActivity()), getProjectData().get(0));
            // add project request to user suggested
            utils.studentSuggestedProjectRequest(getProjectData().get(0), getProjectData().get(1),
                    getProjectData().get(2));
            // send notification to student
            FirestoreUtils.createNotification(utils.getUSER_COLLECTION_PATH(),
                    user.getUserId(requireActivity()), getProjectData().get(0), title, description);

            // update user current info
            Map<String, Object> updatesToStudent = new HashMap<>();
            updatesToStudent.put("supervisor email", null);
            updatesToStudent.put("supervisor name", null);
            dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                    .document(getProjectData().get(0))
                    .update(updatesToStudent)
                    .addOnSuccessListener(unused -> {
                        Log.w("LOGGER", "successfully altered student project supervisor info to null");
                    })
                    .addOnFailureListener(e -> {
                        Log.w("LOGGER", "failed to turn student project info to null: " + e);
                    });

            list.removeProjectRequestFromAdapter(getIndex());
            Toast.makeText(getContext(), "Project passed to other supervisors", Toast.LENGTH_SHORT);
        });
    }

    private void sendProjectAcceptedNotification(String studentId) {
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("sender", user.getUserId(requireActivity()));
        notificationData.put("title", "Project request update!");
        notificationData.put("description", user.getUserId(requireActivity()) +
                " has accepted your request for project supervision. \n" +
                "Please check your My Project for conformation");
        dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                .document(studentId)
                .collection("notifications")
                .add(notificationData)
                .addOnSuccessListener(documentReference -> Log.w("LOGGER", "successfully sent project request update notification"))
                .addOnFailureListener(e -> Log.w("LOGGER", "failed to send project request update notification"));
    }

    private void deleteUserSuggestedProject() {
        dbInstance.collection("student suggested projects")
                .document(projectData.get(0))
                .delete()
                .addOnSuccessListener(unused -> Log.w("LOGGER", "suggested project deleted"))
                .addOnFailureListener(e -> Log.w("LOGGER", "failed to delete suggested project, may be supervisor recommended."));
    }

    private void deleteSupervisorRecommendedProject() {
        dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                .document(user.getUserId(requireActivity()))
                .collection(utils.getSUPERVISOR_REQUESTS_COLLECTION_PATH())
                .document(getProjectData().get(0))
                .delete()
                .addOnSuccessListener(unused -> Log.w("LOGGER", "successfully deleted project request with id " + getProjectData().get(0)))
                .addOnFailureListener(e -> Log.w("LOGGER", "failed to delete project request with id " + getProjectData().get(0)));
    }

    public ArrayList<String> getProjectData() {
        return this.projectData;
    }

    private int getIndex() {
        return this.index;
    }

}
