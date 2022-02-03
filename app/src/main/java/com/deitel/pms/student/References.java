package com.deitel.pms.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.deitel.pms.R;


public class References extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_references, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CheckBox styleAPA = (CheckBox) view.findViewById(R.id.refAPA);
        CheckBox styleOxford = (CheckBox) view.findViewById(R.id.refOxford);
        CheckBox styleACM = (CheckBox) view.findViewById(R.id.refACM);
        CheckBox styleHarvard = (CheckBox) view.findViewById(R.id.refHarvard);
        TextView referenceStyle = (TextView) view.findViewById(R.id.tvRefStyle);

        FragmentManager fm = getChildFragmentManager();


        styleAPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styleOxford.setChecked(false);
                styleACM.setChecked(false);
                styleHarvard.setChecked(false);

                referenceStyle.setText(R.string.aqa);

                fm.beginTransaction()
                        .add(R.id.refStyleFragment, new subReferences("AQA"))
                        .addToBackStack(null).commit();

            }
        });

        styleOxford.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styleAPA.setChecked(false);
                styleACM.setChecked(false);
                styleHarvard.setChecked(false);

                fm.popBackStack();
                fm.beginTransaction()
                        .add(R.id.refStyleFragment, new subReferences("Oxford"))
                        .addToBackStack(null).commit();

            }
        });

        styleACM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styleOxford.setChecked(false);
                styleAPA.setChecked(false);
                styleHarvard.setChecked(false);

                fm.popBackStack();
                fm.beginTransaction()
                        .add(R.id.refStyleFragment, new subReferences("ACM"))
                        .addToBackStack(null).commit();
            }
        });

        styleHarvard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styleOxford.setChecked(false);
                styleACM.setChecked(false);
                styleAPA.setChecked(false);

                fm.popBackStack();
                fm.beginTransaction()
                        .add(R.id.refStyleFragment, new subReferences("Harvard"))
                        .addToBackStack(null).commit();
            }
        });


        Button btnGoBack = (Button) view.findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }
}
