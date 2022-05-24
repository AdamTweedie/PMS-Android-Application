package com.deitel.pms.supervisor;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.student.Calendar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SupervisorWorkspace extends Fragment implements MyStudentsRecyclerViewAdapter.ItemClickListener {

    FirestoreUtils utils = new FirestoreUtils();
    User user = new User();
    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    MyStudentsRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_workspace, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btnProjectRequests = (Button) view.findViewById(R.id.swBtnProjectRequests);
        final Button btnCreateNotification = (Button) view.findViewById(R.id.swBtnCreateNotification);
        final Button btnCreateNewProject = (Button) view.findViewById(R.id.swBtnCreateNewProject);
        final Button btnCalendar = (Button) view.findViewById(R.id.swBtnCalendarView);

        // Load myStudents
        RecyclerView recyclerView = view.findViewById(R.id.rvSupervisorsStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyStudentsRecyclerViewAdapter(getContext(), null);
        dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                .document(user.getUserId(requireActivity()))
                .collection("approved projects")
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot snapshots = task.getResult();
                    ArrayList<String> myStudentArray = new ArrayList<>();
                    for (QueryDocumentSnapshot student : snapshots) {
                        myStudentArray.add(student.getId());
                    }
                    System.out.println("My Students " + myStudentArray);
                    adapter.setmData(myStudentArray);
                    adapter.setClickListener(SupervisorWorkspace.this);
                    recyclerView.setAdapter(adapter);
                    Log.w("LOGGER", "successfully loaded myStudents to RecyclerView");
                }).addOnFailureListener(e -> Log.w("LOGGER", "failed to upload myStudents to RecyclerView"));


        btnProjectRequests.setOnClickListener(view13 -> {

            Fragment fragment = getParentFragmentManager()
                    .findFragmentById(R.id.supervisor_nav_bar_fragment);

            ArrayList<ArrayList<String>> supervisorRecommendedProjects = new ArrayList<>();
            ArrayList<ArrayList<String>> studentSuggestedProjects = new ArrayList<>();
            // get supervisor recommended projects
            dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                    .document(user.getUserId(requireActivity()))
                    .collection(utils.getSUPERVISOR_REQUESTS_COLLECTION_PATH())
                    .get()
                    .addOnCompleteListener(task -> {
                        QuerySnapshot snapshots = task.getResult();
                        for (QueryDocumentSnapshot studentId : snapshots) {
                            ArrayList<String> project = new ArrayList<>();
                            project.add(studentId.getId());
                            project.add(requireNonNull(studentId.get("project title")).toString());
                            project.add(requireNonNull(studentId.get("project description")).toString());
                            project.add(user.getUserId(requireActivity()));

                            supervisorRecommendedProjects.add(project);
                        }

                        System.out.println("supervisor recommended projects " + supervisorRecommendedProjects);

                        //  get student suggested projects
                        dbInstance.collection("student suggested projects")
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    QuerySnapshot snapshots1 = task1.getResult();
                                    for (QueryDocumentSnapshot studentId : snapshots1) {
                                        ArrayList<String> project = new ArrayList<>();
                                        project.add(studentId.getId());
                                        project.add(requireNonNull(studentId.get("project title")).toString());
                                        project.add(requireNonNull(studentId.get("project description")).toString());
                                        project.add((String) studentId.get("supervisor email"));

                                        studentSuggestedProjects.add(project);
                                    }

                                    System.out.println("student suggested projects " + studentSuggestedProjects);

                                    if (fragment!=null) {
                                        getParentFragmentManager().beginTransaction()
                                                .add(R.id.supervisor_nav_bar_fragment,
                                                        new ProjectRequests(supervisorRecommendedProjects,
                                                                studentSuggestedProjects))
                                                .addToBackStack("project requests")
                                                .commit();
                                    }
                                }).addOnFailureListener(e -> {

                        });
                    }).addOnFailureListener(e -> Toast.makeText(getContext(),
                    "Failed to find supervisor recommended project requests",
                    Toast.LENGTH_SHORT).show());
        });


        btnCreateNotification.setOnClickListener(view12 -> getParentFragmentManager()
                .beginTransaction().add(R.id.supervisor_nav_bar_fragment, new CreateNotification())
                .addToBackStack("new notification")
                .commit());

        btnCreateNewProject.setOnClickListener(view1 -> getParentFragmentManager()
                .beginTransaction().add(R.id.supervisor_nav_bar_fragment, new CreateNewProject())
                .addToBackStack("new project")
                .commit());

        btnCalendar.setOnClickListener(view2 -> getParentFragmentManager()
                .beginTransaction()
                .add(R.id.supervisor_nav_bar_fragment, new Calendar())
                .addToBackStack("calendar")
                .commit());

    }


    @Override
    public void onItemClick(View view, int position) {
        // do nothing
    }
}
