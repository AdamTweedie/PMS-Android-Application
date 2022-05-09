package com.deitel.pms.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;

public class SubReferences extends Fragment {

    String style;

    public SubReferences(String refStyle) {
        this.style = refStyle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.child_fragment_reference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView refInfo = (TextView) view.findViewById(R.id.tvRefInfo);

        if (getStyle().equals("ACM")) {
            refInfo.setText("");
            refInfo.setText(R.string.ACMinfo);
            refInfo.setTextSize(20);
        }

        if (getStyle().equals("Oxford")) {
            refInfo.setText("");
            refInfo.setText(R.string.OxfordInfo);
            refInfo.setTextSize(20);
        }

        if (getStyle().equals("Harvard")) {
            refInfo.setText("");
            refInfo.setText(R.string.HarvardInfo);
            refInfo.setTextSize(20);
        }

        if (getStyle().equals("APA")) {
            refInfo.setText("");
            refInfo.setText(R.string.APAInfo);
            refInfo.setTextSize(20);
        }

    }

    public String getStyle() {
        return style;
    }
}
