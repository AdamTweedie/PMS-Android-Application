package com.deitel.pms.recommender;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;
import com.deitel.pms.student.HomeActivity;

public class ProjectSelectionIntro extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_selection_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Button btnStartRecommender = view.findViewById(R.id.psiBtnLoadRecommender);
        Button btnSuggestOwnProject = view.findViewById(R.id.psiBtnSuggestOwnProject);
        Button btnSeeFullProjectList = view.findViewById(R.id.psiBtnSeeFullProjectList);
        Button btnContinueToWorkspace = view.findViewById(R.id.psiBtnContinueToWorkspace);



        btnStartRecommender.setOnClickListener(view1 -> {
            getParentFragmentManager().popBackStack();
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.recommenderContainterView, new UserInterestsInput())
                    .addToBackStack("pickInterests")
                    .commit();
        });

        btnSuggestOwnProject.setOnClickListener(view12 -> getParentFragmentManager()
                .beginTransaction()
                .add(R.id.recommenderContainterView, new ProjectRequestForm())
                .addToBackStack("projectRequestForm")
                .commit());

        btnSeeFullProjectList.setOnClickListener(view13 -> {
            getParentFragmentManager().popBackStack();
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.recommenderContainterView, new FullProjectList())
                    .addToBackStack("projectRequestForm")
                    .commit();
        });

        btnContinueToWorkspace.setOnClickListener(view14 -> {
            getParentFragmentManager().popBackStack();
            Intent homeScreen = new Intent(getActivity(), HomeActivity.class);
            startActivity(homeScreen);
            requireActivity().finish();
        });
    }
}
