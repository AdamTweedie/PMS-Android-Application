package com.deitel.pms.admin;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.supervisor.MyStudentsRecyclerViewAdapter;

import java.util.ArrayList;

public class AdminStudentSupervisorView extends Fragment implements MyStudentsRecyclerViewAdapter.ItemClickListener {

    MyStudentsRecyclerViewAdapter adapter;

    private boolean student;
    private ArrayList<String> userData;
    AdminStudentSupervisorView(ArrayList<String> data, Boolean isStudent) {
        this.student = isStudent;
        this.userData = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_notifications, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyStudentsRecyclerViewAdapter(context, this.userData);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
