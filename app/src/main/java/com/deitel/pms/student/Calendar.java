package com.deitel.pms.student;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;

import java.util.ArrayList;
import java.util.Date;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class Calendar extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_calendar, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // functionality
        final ImageButton btnGoBack = (ImageButton) view.findViewById(R.id.btnGoBack);
        final Button addDateToCalendar = (Button) view.findViewById(R.id.btnAddDate);
        final CheckBox reoccurringDate = (CheckBox) view.findViewById(R.id.repeatDate);
        EditText day = (EditText) view.findViewById(R.id.calendarDay);
        EditText month = (EditText) view.findViewById(R.id.calendarMonth);
        EditText year = (EditText) view.findViewById(R.id.calendarYear);
        EditText calendarNote = (EditText) view.findViewById(R.id.calendarMarkDayNote);

        MCalendarView calendarView = (MCalendarView) view.findViewById(R.id.calendarView);

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                day.setText(String.valueOf(date.getDay()));
                month.setText(String.valueOf(date.getMonth()));
                year.setText(String.valueOf(date.getYear()));
                DateData dateData = new DateData(date.getYear(), date.getMonth(), date.getDay());
                addDateToCalendar.setOnClickListener(view1 -> {
                    calendarView.markDate(date.getYear(), date.getMonth(), date.getDay());

                });
            }
        });

        Log.d("marked dates:-","" + calendarView.getMarkedDates()); //get all marked dates.


        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

}
