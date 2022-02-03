package com.deitel.pms.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;

public class subReferences extends Fragment {

    String style;

    public subReferences(String refStyle) {
        this.style = refStyle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getStyle().equals("APA")) {

        }

        if (getStyle().equals("APA")) {

        }

        if (getStyle().equals("Harvard")) {

        }

        if (getStyle().equals("Oxford")) {

        }

    }

    public String getStyle() {
        return style;
    }
}
