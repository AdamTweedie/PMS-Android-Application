package com.deitel.pms.recommender;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deitel.pms.R;
import com.deitel.pms.SignUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserInterestsInput extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_project_interests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btnConfirm = (Button) view.findViewById(R.id.btnConfirmChoices);
        final ImageButton btnPopFragment = (ImageButton) view.findViewById(R.id.fpiPopFragment);
        EditText subjectInterests = (EditText) view.findViewById(R.id.etOtherInformation);

        btnPopFragment.setOnClickListener(view1 -> {
            try {
                getParentFragmentManager().popBackStack();
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.recommenderContainterView, new ProjectSelectionIntro())
                        .addToBackStack("project selection intro")
                        .commit();
            } catch (Exception e) {
                Log.e("LOGGER", "failed to po fragment with exception " + e);
            }
        });


        btnConfirm.setOnClickListener(view12 -> {
            String dfEntry = subjectInterests.getText().toString();

            if (dfEntry.length()>3) {
                Bundle result = new Bundle();
                result.putString("bundleKey", dfEntry);
                getParentFragmentManager().setFragmentResult("requestKey", result);

                final FragmentTransaction fragmentTransaction;
                final FragmentManager fragmentManager = getParentFragmentManager();
                final Fragment fragment = getParentFragmentManager()
                        .findFragmentById(R.id.recommenderContainterView);
                if (fragment != null) {
                    fragmentTransaction = fragmentManager.beginTransaction().remove(fragment);
                    fragmentTransaction.commit();
                }
            } else {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
