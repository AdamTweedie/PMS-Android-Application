package com.deitel.pms.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
        ImageButton btnGoBack = (ImageButton) view.findViewById(R.id.refBtnGoBack);

        FragmentManager fm = getChildFragmentManager();


        styleAPA.setOnClickListener(view1 -> {
            styleOxford.setChecked(false);
            styleACM.setChecked(false);
            styleHarvard.setChecked(false);

            referenceStyle.setText(R.string.APA);

            fm.popBackStack();
            fm.beginTransaction()
                    .add(R.id.refStyleFragment, new SubReferences("APA"))
                    .addToBackStack(null).commit();

        });

        styleOxford.setOnClickListener(view12 -> {
            styleAPA.setChecked(false);
            styleACM.setChecked(false);
            styleHarvard.setChecked(false);

            referenceStyle.setText(R.string.oxford);

            fm.popBackStack();
            fm.beginTransaction()
                    .add(R.id.refStyleFragment, new SubReferences("Oxford"))
                    .addToBackStack(null).commit();

        });

        styleACM.setOnClickListener(view13 -> {
            styleOxford.setChecked(false);
            styleAPA.setChecked(false);
            styleHarvard.setChecked(false);

            referenceStyle.setText(R.string.acm);

            fm.popBackStack();
            fm.beginTransaction()
                    .add(R.id.refStyleFragment, new SubReferences("ACM"))
                    .addToBackStack(null).commit();
        });

        styleHarvard.setOnClickListener(view14 -> {
            styleOxford.setChecked(false);
            styleACM.setChecked(false);
            styleAPA.setChecked(false);

            referenceStyle.setText(R.string.harvard);

            fm.popBackStack();
            fm.beginTransaction()
                    .add(R.id.refStyleFragment, new SubReferences("Harvard"))
                    .addToBackStack(null).commit();
        });


        btnGoBack.setOnClickListener(view15 -> getParentFragmentManager().popBackStack());
    }
}
