package com.deitel.pms.recommender;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.student.HomeActivity;

public class ProjectRequestForm extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_suggest_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText suggestedTitle = (EditText) view.findViewById(R.id.fspEtProjectTitle);
        EditText suggestedDescription = (EditText) view.findViewById(R.id.fspTvProjectDescription);
        Button btnRequestProject = (Button) view.findViewById(R.id.fspBtnRequestProject);

        btnRequestProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = suggestedTitle.getText().toString();
                final String description = suggestedDescription.getText().toString();
                final FirestoreUtils firestoreUtils = new FirestoreUtils();
                final User user = new User();

                firestoreUtils.addUserProject(requireActivity(),
                        null, null, title, description);
                firestoreUtils.studentSuggestedProjectRequest(user.getUserId(requireActivity()), title, description);

                getParentFragmentManager().popBackStack();
                Intent homeScreen = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeScreen);
                Toast.makeText(getContext(), "Project request received !", Toast.LENGTH_SHORT).show();
                requireActivity().finish();

            }
        });

    }
}
