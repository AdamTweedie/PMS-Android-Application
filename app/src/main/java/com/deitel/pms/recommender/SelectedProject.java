package com.deitel.pms.recommender;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentManager;

import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SelectedProject extends Fragment {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    final String projectTitle;
    final String projectDescription;
    final String supervisorName;
    final String supervisorEmail;

    public SelectedProject(String title, String description, String name, String email) {
        this.projectTitle = title;
        this.projectDescription = description;
        this.supervisorName = name;
        this.supervisorEmail = email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_selected_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getParentFragmentManager();

        ImageButton btnBackToSuggestions = view.findViewById(R.id.btnBackToSuggestions);
        Button btnRequestProject = view.findViewById(R.id.btRequestProject);

        TextView projectTitle = view.findViewById(R.id.tvProjectTitle);
        TextView projectDescription = view.findViewById(R.id.tvProjectDescription);
        TextView supervisorName = view.findViewById(R.id.tvSupervisorName);
        TextView supervisorEmail = view.findViewById(R.id.tvSupervisorEmail);

        projectTitle.setText(getProjectTitle());
        projectDescription.setText(getProjectDescription());
        supervisorName.setText(getSupervisorName());
        supervisorEmail.setText(getSupervisorEmail());

        btnBackToSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
            }
        });

        btnRequestProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO - add a confirm project request before progressing
                // TODO - add project to user profile on firestore with an 'approved column' set to False
                System.out.println("User id - " + getUserId());
                addToFirestore(getUserId(), getSupervisorEmail(), getSupervisorName(), getProjectTitle(), getProjectDescription());

            }
        });

    }

    private String getProjectTitle() {
        return this.projectTitle;
    }

    private String getProjectDescription() {
        return this.projectDescription;
    }

    private String getSupervisorName() {
        return this.supervisorName;
    }

    private String getSupervisorEmail() {
        return this.supervisorEmail;
    }

    private void addToFirestore(String userId,
                                String supervisorEmail,
                                String supervisorName,
                                String projectTitle,
                                String projectDescription) {

        final String TAG = "Add project";

        Map<String, Object> project = new HashMap<>();
        project.put("supervisor email", supervisorEmail);
        project.put("supervisor name", supervisorName);
        project.put("project title", projectTitle);
        project.put("project description", projectDescription);
        project.put("approved project", "false");

        db.collection("users")
                .document(userId)
                .set(project, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + userId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = (SharedPreferences) requireActivity()
                .getSharedPreferences("user_id", Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "No User Id");
    }
}
