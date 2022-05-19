package com.deitel.pms.recommender;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    ArrayList<ArrayList<String>> projectsArray = new ArrayList<>();
    ArrayList<ArrayList<String>> completeProjectSet = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.full_project_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText keywordEnter = (EditText) view.findViewById(R.id.fplEditTextKeywordSearch);
        final Button searchButton = (Button) view.findViewById(R.id.fplBtnSearchKeyword);
        final ImageButton refreshProjects = (ImageButton) view.findViewById(R.id.fplBtnRefreshProjects);
        final ImageButton undo = (ImageButton) view.findViewById(R.id.fplBtnUndo);

        final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        RecyclerView recyclerView;

        LoadingSuggestions(true);

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getParentFragmentManager().popBackStack();
                    getParentFragmentManager()
                            .beginTransaction()
                            .add(R.id.recommenderContainterView, new ProjectSelectionIntro())
                            .addToBackStack("project selection intro")
                            .commit();
                } catch (Exception e) {
                    Log.e("LOGGER", "failed to po fragment with exception " + e);
                }
            }
        });

        searchButton.setOnClickListener(view1 -> {
            String keyword = keywordEnter.getText().toString();
            ArrayList<Integer> searchResults = KeyWordProjectSearch.Search(completeProjectSet, keyword);
            System.out.println("KEYWORD SEARCH RESULTS - " + searchResults);
            ArrayList<ArrayList<String>> newProjectsList = new ArrayList<>();
            if (searchResults != null) {
                for (int projectIndex : searchResults) {
                    newProjectsList.add(completeProjectSet.get(projectIndex));
                }
                updateAdapter(newProjectsList);
            }
        });

        refreshProjects.setOnClickListener(view2 -> {
            updateAdapter(completeProjectSet);
        });

        try {
            recyclerView = requireView().findViewById(R.id.fullProjectListRecyclerView);
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

                                projectsArray.add(project);
                                completeProjectSet.add(project);
                            }
                            LoadingSuggestions(false);
                            adapter = new FullProjectListRecyclerViewAdapter(getContext(), projectsArray);
                            adapter.setClickListener(FullProjectList.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LoadingSuggestions(false);
                    Toast.makeText(getContext(), "Cannot get projects, check internet connection !", Toast.LENGTH_SHORT).show();
                    adapter = new FullProjectListRecyclerViewAdapter(getContext(), projectsArray);
                    adapter.setClickListener(FullProjectList.this);
                    recyclerView.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            Log.e("LOGGER", "Failed to create recycler view with exception" + e);
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

    private void LoadingSuggestions(Boolean load) {
        ProgressBar progress = requireView().findViewById(R.id.progressBar_cyclic);
        if (load) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
        }
    }

    public void updateAdapter(ArrayList<ArrayList<String>> projects) {
        this.projectsArray.clear();
        this.projectsArray.addAll(projects);
        adapter.notifyDataSetChanged();
    }
}
