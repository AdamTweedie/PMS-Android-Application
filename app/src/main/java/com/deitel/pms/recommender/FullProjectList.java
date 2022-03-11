package com.deitel.pms.recommender;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FullProjectList extends Fragment implements FullProjectListRecyclerViewAdapter.ItemClickListener{

    FullProjectListRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.full_project_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageButton undo = (ImageButton) view.findViewById(R.id.fplBtnUndo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.recommenderContainterView, new ProjectSelectionIntro())
                        .addToBackStack("project selection intro")
                        .commit();
            }
        });

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(requireContext()));
        }

        Python py = Python.getInstance();
        new getProjectsThread().execute(py);
        LoadingSuggestions(true);

    }

    private class getProjectsThread extends AsyncTask<Python, Void, ArrayList<ArrayList<String>>> {

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Python... pythons) {

            PyObject module = pythons[0].getModule("getProjects");

            ArrayList<ArrayList<String>> projectsArray = new ArrayList<>();
            List<PyObject> pyObjectList = module.callAttr("get_all_projects").asList();

            for (PyObject objList : pyObjectList) {
                ArrayList<String> project = new ArrayList<>();
                for (PyObject objItem : objList.asList()) {
                    project.add(objItem.toString());
                }
                projectsArray.add(project);
            }

            return projectsArray;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> results) {
            super.onPostExecute(results);

            FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
            RecyclerView recyclerView = requireView().findViewById(R.id.fullProjectListRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            dbInstance.collection("New Projects")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<String> project = new ArrayList<>();
                        // Email,Name,Project title,Brief description,Any other useful information
                        project.add((String) document.get("supervisor email"));
                        project.add((String) document.get("supervisor name"));
                        project.add((String) document.get("project title"));
                        project.add((String) document.get("project description"));
                        project.add((String) document.get("other info"));

                        results.add(project);
                    }
                    LoadingSuggestions(false);
                    adapter = new FullProjectListRecyclerViewAdapter(getContext(), results);
                    adapter.setClickListener(FullProjectList.this);
                    recyclerView.setAdapter(adapter);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LoadingSuggestions(false);
                    adapter = new FullProjectListRecyclerViewAdapter(getContext(), results);
                    adapter.setClickListener(FullProjectList.this);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked on row number " + position, Toast.LENGTH_SHORT).show();
        ArrayList<String> projectData = adapter.getItem(position);

        getParentFragmentManager().beginTransaction()
                .add(R.id.recommenderContainterView, new SelectedProject(projectData.get(2),
                        projectData.get(3),
                        projectData.get(1),
                        projectData.get(0))).addToBackStack("project").commit();
    }

    public void LoadingSuggestions(Boolean load) {
        ProgressBar progress = requireView().findViewById(R.id.progressBar_cyclic);
        if (load) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
        }
    }
}
