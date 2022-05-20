package com.deitel.pms.admin;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminWorkspace extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_workspace_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnStudents = (Button) view.findViewById(R.id.adminBtnViewStudents);
        Button btnSupervisors = (Button) view.findViewById(R.id.adminBtnViewSupervisors);
        Button btnCreateNotification = (Button) view.findViewById(R.id.adminBtnCreateNotification);

        btnStudents.setOnClickListener(view1 -> {
            loadUsersView("users", true);
        });

        btnSupervisors.setOnClickListener(view2 -> {
            loadUsersView("supervisors", false);
        });

        btnCreateNotification.setOnClickListener(view3 -> {

        });
    }

    private void loadUsersView(String collectionPath, Boolean isStudent) {
        FirebaseFirestore
                .getInstance()
                .collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<String> data = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        data.add(document.getId());
                    }
                    System.out.println(data);
                    Fragment fragment = getParentFragmentManager()
                            .findFragmentById(R.id.admin_nav_bar_fragment);
                    if (fragment!=null) {
                        getParentFragmentManager().beginTransaction()
                                .add(R.id.admin_nav_bar_fragment,
                                        new AdminStudentSupervisorView(data, isStudent))
                                .addToBackStack("viewer")
                                .commit();
                    }
                });
    }
}
