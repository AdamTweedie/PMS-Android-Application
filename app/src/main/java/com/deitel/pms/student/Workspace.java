package com.deitel.pms.student;

import android.os.Build;
import android.os.Bundle;
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
import com.deitel.pms.student.kanban.KanbanBoard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Workspace extends Fragment {


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

        Button btnCalendar = (Button) view.findViewById(R.id.btnCalendarView);
        TextView tvDateMonth = (TextView) view.findViewById(R.id.tvCurrentDateMonth);
        TextView tvDateDay = (TextView) view.findViewById(R.id.tvCurrentDateDay);
        Button btnReferences = (Button) view.findViewById(R.id.btnReferencingGuide);
        Button btnKanban = (Button) view.findViewById(R.id.btnKanbanBoard);
        Button btnMyProject = (Button) view.findViewById(R.id.btnMyProject);

        btnMyProject.setOnClickListener(view1 -> {
            Fragment fragment = getParentFragmentManager()
                    .findFragmentById(R.id.nav_bar_fragment);
            if (fragment!=null) {
                getParentFragmentManager().beginTransaction()
                        .add(R.id.nav_bar_fragment, new MyProject())
                        .addToBackStack("kanban")
                        .commit();
            }
        });

        btnKanban.setOnClickListener(view12 -> {
            Fragment fragment = getParentFragmentManager()
                    .findFragmentById(R.id.nav_bar_fragment);
            if (fragment!=null) {
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().beginTransaction()
                        .add(R.id.nav_bar_fragment, new KanbanBoard())
                        .addToBackStack("kanban")
                        .commit();
            }
        });

        try {
            DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MMM");
            DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("dd");
            LocalDateTime now = LocalDateTime.now();
            tvDateMonth.setText(dtfMonth.format(now));
            tvDateDay.setText(dtfDay.format(now));
        } catch (RuntimeException ignored) {}

        // functionality
        btnCalendar.setOnClickListener(view13 -> {
            Fragment fragment = getParentFragmentManager()
                    .findFragmentById(R.id.nav_bar_fragment);

            if (fragment!=null) {
                getParentFragmentManager().beginTransaction()
                        .add(R.id.nav_bar_fragment, new Calendar())
                        .addToBackStack("calendar")
                        .commit();
            }
        });

        // functionality
        btnReferences.setOnClickListener(view14 -> {
            Fragment fragment = getParentFragmentManager()
                    .findFragmentById(R.id.nav_bar_fragment);

            if (fragment!=null) {
                getParentFragmentManager().beginTransaction()
                        .add(R.id.nav_bar_fragment, new References())
                        .addToBackStack("references")
                        .commit();
            }
        });
    }
}
