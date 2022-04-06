package com.deitel.pms.student;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ImageButton btnGoBack;


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
        btnGoBack = (ImageButton) view.findViewById(R.id.btnGoBack);
        EditText day = (EditText) view.findViewById(R.id.calendarDay);
        EditText month = (EditText) view.findViewById(R.id.calendarMonth);
        EditText year = (EditText) view.findViewById(R.id.calendarYear);

        MCalendarView calendarView = (MCalendarView) view.findViewById(R.id.calendarView);

        ArrayList<DateData> dates = new ArrayList<>();

        dates.add(new DateData(2022,05,9));
        dates.add(new DateData(2022,05,7));

        for (DateData date : dates) {
            calendarView.markDate(date.getYear(), date.getMonth(), date.getDay());
        }

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                day.setText(String.valueOf(date.getDay()));
                month.setText(String.valueOf(date.getMonth()));
                year.setText(String.valueOf(date.getYear()));
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
