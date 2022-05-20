package com.deitel.pms.admin;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.student.ExpandedNotification;
import com.deitel.pms.student.Notifications;
import com.deitel.pms.supervisor.MyStudentsRecyclerViewAdapter;
import com.deitel.pms.supervisor.SupervisorWorkspace;
import com.google.firebase.firestore.FirebaseFirestore;

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
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(AdminStudentSupervisorView.this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        if (this.student) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(adapter.getItem(position))
                    .get()
                    .addOnCompleteListener(task -> {
                        String projectTitle = (String) task.getResult().get("project title");
                        String supervisorEmail = (String) task.getResult().get("supervisor email");
                        Boolean projectApproved = (Boolean) task.getResult().get("approved project");
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.admin_nav_bar_fragment,
                                        new StudentExpandedView(adapter.getItem(position),
                                                projectTitle, supervisorEmail, projectApproved))
                                .addToBackStack("expanded notification")
                                .commit();
                    }).addOnFailureListener(e -> Log.e("LOGGER", "Failed to load student data"));
        } else {
//            FirebaseFirestore.getInstance()
//                    .collection("supervisors")
//                    .document(adapter.getItem(position))
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        String projectTitle = (String) task.getResult().get("project title");
//                        String supervisorEmail = (String) task.getResult().get("supervisor email");
//                        Boolean projectApproved = (Boolean) task.getResult().get("approved project");
//                        requireActivity().getSupportFragmentManager().beginTransaction()
//                                .add(R.id.nav_bar_fragment,
//                                        new StudentExpandedView(adapter.getItem(position),
//                                                projectTitle, supervisorEmail, projectApproved))
//                                .addToBackStack("expanded notification")
//                                .commit();
//                    }).addOnFailureListener(e -> Log.e("LOGGER", "Failed to load student data"));
        }
    }
}
