package com.deitel.pms.supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNewProject extends Fragment {

    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_new_project_idea, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageButton collapseProjectForm = (ImageButton) view.findViewById(R.id.snpiUndo);
        final TextView supervisorEmail = (TextView) view.findViewById(R.id.snpiSupervisorEmailId);
        final EditText supervisorName = (EditText) view.findViewById(R.id.snpiEtSupervisorName);
        final EditText projectTitle = (EditText) view.findViewById(R.id.snpiEtProjectTitle);
        final EditText projectDescription = (EditText) view.findViewById(R.id.snpiEtProjectDescription);
        final EditText otherUsefulInfo = (EditText) view.findViewById(R.id.snpiEtOtherInfo);
        final Button uploadProjectSuggestion = (Button) view.findViewById(R.id.btnUploadProjectSuggestion);
        final User user = new User();


        collapseProjectForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Set supervisorEmail
        try {
            supervisorEmail.setText(user.getUserId(requireActivity()));
        } catch (Exception e) {
            Log.w("LOGGER", "Failed to display Supervisor Email with exception " + e);
        }


        // Upload project suggestion
        uploadProjectSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = supervisorName.getText().toString();
                final String title = projectTitle.getText().toString();
                final String description = projectDescription.getText().toString();
                final String otherInfo = otherUsefulInfo.getText().toString();

                if (title.length() > 20) {
                    Map<String, Object> newProject = new HashMap<>();
                    newProject.put("supervisor name", name);
                    newProject.put("supervisor email", user.getUserId(requireActivity()));
                    newProject.put("project title", title);
                    newProject.put("project description", description);
                    newProject.put("other info", otherInfo);
                    dbInstance.collection("New Projects")
                            .add(newProject)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.w("LOGGER", "Successfully added new project");
                                    Toast.makeText(getContext(), "Project successfully added!", Toast.LENGTH_SHORT).show();
                                    getParentFragmentManager().popBackStack();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("LOGGER", "Failed to add new project");
                            Toast.makeText(getContext(), "Failed to add project!", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Title insufficient length", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
