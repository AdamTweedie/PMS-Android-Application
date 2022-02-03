package com.deitel.pms.student;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Workspace extends Fragment {

    private Button btnCalendar;
    private Button btnReferences;
    private TextView tvDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_workspace, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCalendar = (Button) view.findViewById(R.id.btnCalendarView);
        tvDate = (TextView) view.findViewById(R.id.tvCurrentDate);
        btnReferences = (Button) view.findViewById(R.id.btnReferencingGuide);

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            tvDate.setText(dtf.format(now));
        } catch (RuntimeException ignored) {}

        // functionality
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getParentFragmentManager()
                        .findFragmentById(R.id.nav_bar_fragment);

                if (fragment!=null) {
                    getParentFragmentManager().beginTransaction()
                            .add(R.id.nav_bar_fragment, new Calendar())
                            .addToBackStack("calendar")
                            .commit();
                }
            }
        });

        // functionality
        btnReferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getParentFragmentManager()
                        .findFragmentById(R.id.nav_bar_fragment);

                if (fragment!=null) {
                    getParentFragmentManager().beginTransaction()
                            .add(R.id.nav_bar_fragment, new References())
                            .addToBackStack("references")
                            .commit();
                }
            }
        });
    }
}
