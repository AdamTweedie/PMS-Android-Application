package com.deitel.pms.supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

    public ExpandedProjectRequest(ArrayList<String> data) {
        this.projectData = data;
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

        tvStudentId.setText(this.projectData.get(0) + " would like you to supervise: ");
        tvProjTitle.setText(this.projectData.get(1));
        tvProjDescription.setText(this.projectData.get(2));

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
                studentUpdates.put(utils.getFIELD_PROJECT_APPROVED(), true);
                dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                        .document(getProjectData().get(0)).set(studentUpdates, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w("LOGGER", "successfully set project approval to true");

                        // Add student to supervisors myStudents collection
                        Map<String, Object> newMyStudent = new HashMap<>();
                        newMyStudent.put("project title", getProjectData().get(1));
                        newMyStudent.put("project description", getProjectData().get(2));

                        dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                                .document(user.getUserId(requireActivity()))
                                .collection("approved projects")
                                .document(getProjectData().get(0))
                                .set(newMyStudent)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        // delete project request
                                        dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                                                .document(user.getUserId(requireActivity()))
                                                .collection(utils.getSUPERVISOR_REQUESTS_COLLECTION_PATH())
                                                .document(getProjectData().get(0))
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.w("LOGGER", "successfully deleted project request with id " + getProjectData().get(0));
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("LOGGER", "failed to delete project request with id " + getProjectData().get(0));
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("LOGGER", "failed to add student to my projects");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGGER", "Failed to set project approval to true");
                    }
                });
            }
        });
    }

    public ArrayList<String> getProjectData() {
        return this.projectData;
    }

}
