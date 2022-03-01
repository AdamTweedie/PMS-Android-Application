package com.deitel.pms.supervisor;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.student.Calendar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SupervisorWorkspace extends Fragment {

    FirestoreUtils utils = new FirestoreUtils();
    User user = new User();
    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_workspace, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Button btnProjectRequests = (Button) view.findViewById(R.id.swBtnProjectRequests);
        btnProjectRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = getParentFragmentManager()
                        .findFragmentById(R.id.supervisor_nav_bar_fragment);

                ArrayList<ArrayList<String>> projectRequests = new ArrayList<>();
                dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                        .document(user.getUserId(requireActivity()))
                        .collection(utils.getSUPERVISOR_REQUESTS_COLLECTION_PATH())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshots = task.getResult();
                        for (QueryDocumentSnapshot studentId : snapshots) {
                            ArrayList<String> project = new ArrayList<>();
                            project.add(studentId.getId());
                            project.add(requireNonNull(studentId.get("project title")).toString());
                            project.add(requireNonNull(studentId.get("project description")).toString());

                            projectRequests.add(project);
                        }

                        if (fragment!=null) {
                            getParentFragmentManager().beginTransaction()
                                    .add(R.id.supervisor_nav_bar_fragment, new ProjectRequests(projectRequests))
                                    .addToBackStack("project requests")
                                    .commit();
                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to find project requests", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
