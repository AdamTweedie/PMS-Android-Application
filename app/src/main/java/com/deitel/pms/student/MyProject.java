package com.deitel.pms.student;

import android.content.Intent;
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
import com.deitel.pms.recommender.RecommenderActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class MyProject extends Fragment {

    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    FirestoreUtils u = new FirestoreUtils();
    User user = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_project_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String userId = user.getUserId(requireActivity());

        ImageButton btnPopFragment = (ImageButton) view.findViewById(R.id.btnPopFragment);
        Button btnChangeProject = (Button) view.findViewById(R.id.btnChangeProject);
        TextView projectTitle = (TextView) view.findViewById(R.id.mpdProjectTitle);
        TextView projectDescription = (TextView) view.findViewById(R.id.mpdProjectDescription);
        TextView supervisorName = (TextView) view.findViewById(R.id.mpdSupervisorName);
        TextView supervisorEmail = (TextView) view.findViewById(R.id.mpdSupervisorEmail);
        TextView projectApprovalStatus = (TextView) view.findViewById(R.id.mpdProjectApprovalStatus);

        btnPopFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.nav_bar_fragment);
                if (fragment != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        DocumentReference docRef = dbInstance.collection(u.getUSER_COLLECTION_PATH()).document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                try {
                    projectTitle.setText(document.getString(u.getFIELD_PROJECT_TITLE()));
                    projectDescription.setText(document.getString(u.getFIELD_PROJECT_DESCRIPTION()));
                    projectApprovalStatus.setText("Approved: " + document.getBoolean(u.getFIELD_PROJECT_APPROVED()).toString());
                    if (document.getBoolean(u.getFIELD_PROJECT_APPROVED())==null || !document.getBoolean(u.getFIELD_PROJECT_APPROVED())) {
                        btnChangeProject.setVisibility(View.VISIBLE);
                        btnChangeProject.setText("Change project");
                        btnChangeProject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getParentFragmentManager().popBackStack();
                                Intent recommender = new Intent(getActivity(), RecommenderActivity.class);
                                startActivity(recommender);
                                requireActivity().finish();
                            }
                        });
                    }
                    supervisorEmail.setText(document.getString(u.getFIELD_SUPERVISOR_EMAIL()));
                } catch (Exception e) {
                    Log.e("LOGGER", "failed with exception " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("LOGGER", "get failed with " + e);
                Toast.makeText(getContext(), "No connection to database !", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
