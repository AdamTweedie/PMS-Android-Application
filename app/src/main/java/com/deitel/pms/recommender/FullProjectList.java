package com.deitel.pms.recommender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.deitel.pms.R;
import com.deitel.pms.supervisor.ProjectRequests;
import com.deitel.pms.supervisor.ProjectRequestsRecyclerViewAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FullProjectList extends Fragment implements FullProjectListRecyclerViewAdapter.ItemClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.full_project_list, container, false);
    }

    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        RecyclerView recyclerView = view.findViewById(R.id.rvProjectRequests);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        FullProjectListRecyclerViewAdapter adapter = new FullProjectListRecyclerViewAdapter(getContext(), null);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(requireContext()));
        }

        Python py = Python.getInstance();
        PyObject module = py.getModule("getProjects");

        List<PyObject> pyObjectList = module.callAttr("get_all_projects").asList();

        ArrayList<ArrayList<String>> projects = new ArrayList<>();

        for (PyObject objList : pyObjectList) {
            ArrayList<String> project = new ArrayList<>();
            for (PyObject objItem : objList.asList()) {
                project.add(objItem.toString());
            }
            projects.add(project);
        }

        System.out.println("Projects Size: " + projects.get(0).get(0));
        // load this into recycler view and also add additional projects from firebase

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
