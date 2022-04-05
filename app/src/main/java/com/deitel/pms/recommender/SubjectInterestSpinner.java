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

public class SubjectInterestSpinner extends Fragment {

    // TODO - get uni id and course code from Student df in FireStore

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_project_interests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        final Spinner spinner3 = (Spinner) view.findViewById(R.id.spinner3);
        final Spinner spinner4 = (Spinner) view.findViewById(R.id.spinner4);
        SpinnerArrayAdapter arrayAdapter1 = new SpinnerArrayAdapter();
        SpinnerArrayAdapter arrayAdapter2 = new SpinnerArrayAdapter();
        SpinnerArrayAdapter arrayAdapter3 = new SpinnerArrayAdapter();
        SpinnerArrayAdapter arrayAdapter4 = new SpinnerArrayAdapter();
        final Button btnConfirm = (Button) view.findViewById(R.id.btnConfirmChoices);
        final ImageButton btnPopFragment = (ImageButton) view.findViewById(R.id.fpiPopFragment);
        EditText otherInfo = (EditText) view.findViewById(R.id.etOtherInformation);
        RecommenderActivity activity = new RecommenderActivity();


        // TODO - if (subject(from firestore)) -> subject keyword fill into spinner

        String[] interest1 = new String[]{
                "Select an item...",
                "AI",
                "Machine Learning",
                "Social Media",
                "Neural Networks",
                "Security"
        };
        final List<String> interest1List = new ArrayList<>(Arrays.asList(interest1));
        ArrayAdapter<String> interest1Adapter = arrayAdapter1.getArrayAdapter(getContext(), interest1List);
        interest1Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(interest1Adapter);

        String[] interest2 = new String[]{
                "Select an item...",
                "Android Studio",
                "Java",
                "Python",
                "Web Development",
                "Data mining",
                "Mobile phones"
        };
        final List<String> interest2List = new ArrayList<>(Arrays.asList(interest2));
        ArrayAdapter<String> interest2Adapter = arrayAdapter2.getArrayAdapter(getContext(), interest2List);
        interest2Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(interest2Adapter);

        String[] interest3 = new String[]{
                "Select an item...",
                "wireless technology",
                "Autonomy",
                "Social Media",
                "Neural Networks",
                "Security",
                "Analysis, design, and implementation"
        };
        final List<String> interest3List = new ArrayList<>(Arrays.asList(interest3));
        ArrayAdapter<String> interest3Adapter = arrayAdapter3.getArrayAdapter(getContext(), interest3List);
        interest3Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setAdapter(interest3Adapter);

        String[] interest4 = new String[]{
                "Select an item...",
                "Security",
                "Cyber threats",
                "policies",
                "interactivity",
                "monitoring",
                "complex networks"
        };
        final List<String> interest4List = new ArrayList<>(Arrays.asList(interest4));
        ArrayAdapter<String> interest4Adapter = arrayAdapter4.getArrayAdapter(getContext(), interest4List);
        interest4Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner4.setAdapter(interest4Adapter);


        arrayAdapter1.listener(spinner1, getContext());
        arrayAdapter2.listener(spinner2, getContext());
        arrayAdapter3.listener(spinner3, getContext());
        arrayAdapter4.listener(spinner4, getContext());

        btnPopFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item1 = arrayAdapter1.getSelectedItem();
                String item2 = arrayAdapter2.getSelectedItem();
                String item3 = arrayAdapter3.getSelectedItem();
                String item4 = arrayAdapter4.getSelectedItem();
                String anyOtherInfo = otherInfo.getText().toString();

                String dfEntry = item1 + " " + item2 + " " + item3 + " " + item4 + " " + anyOtherInfo;
                System.out.println("DF ENTRY: " + dfEntry);

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
            }
        });
    }
}
