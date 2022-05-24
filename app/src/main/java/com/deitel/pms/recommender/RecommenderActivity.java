package com.deitel.pms.recommender;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.deitel.pms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommenderActivity extends AppCompatActivity {

    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        FragmentTransaction signInFragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recommenderContainterView, new ProjectSelectionIntro()).addToBackStack("intro");
        signInFragmentTransaction.commit();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {

            String result = bundle.getString("bundleKey");
            System.out.println("this is the result in activity " + result);

            // String cleanEntry = sortResult(result)

            ArrayList<ArrayList<String>> data = new ArrayList<>();
            ArrayList<String> resultAsArray = new ArrayList<>();
            resultAsArray.add(result);
            data.add(resultAsArray);
            dbInstance.collection("New Projects")
                    .get().addOnCompleteListener(task -> {

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            ArrayList<String> project_data_raw = new ArrayList<>();
                            project_data_raw.add((String) snapshot.get("supervisor email"));
                            project_data_raw.add((String) snapshot.get("supervisor name"));
                            project_data_raw.add((String) snapshot.get("project title"));
                            project_data_raw.add((String) snapshot.get("project description"));
                            project_data_raw.add((String) snapshot.get("other info"));
                            data.add(project_data_raw);
                        }
                        Log.w("LOGGER", "Successfully got additional projects from Firestore");
                        new RecommenderThread().execute(data);
                        LoadingSuggestions(true);
                    }).addOnFailureListener(e -> {
                        Log.w("LOGGER", "failed to get additional projects from Firestore");
                        new RecommenderThread().execute(data);
                        LoadingSuggestions(true);
                    });
        });
    }


    private class RecommenderThread extends AsyncTask<ArrayList<ArrayList<String>>, Void, ArrayList<ArrayList<String>>> {

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(ArrayList<ArrayList<String>>... arrayLists) {

            String userEntry = arrayLists[0].get(0).get(0);
            arrayLists[0].remove(arrayLists[0].get(0));
            ArrayList<ArrayList<String>> d = new ArrayList<>(arrayLists[0]);
            final ArrayList<Object[]> projectData = new ArrayList<>();
            for (ArrayList<String> project : d) {
                projectData.add(project.toArray());
            }

            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(getApplicationContext()));
            }

            Python py = Python.getInstance();

            PyObject module = py.getModule("Recommender");
            List<PyObject> pyIndexList = module.callAttr("recommender", userEntry, projectData.toArray()).asList();

            ArrayList<Integer> indexList = new ArrayList<>();
            ArrayList<ArrayList<String>> projects = new ArrayList<>();

            for (PyObject pyObj : pyIndexList) {
                indexList.add(pyObj.hashCode());
            }
            System.out.println("INDEX LIST = " + indexList);

            for (int index : indexList) {
                ArrayList<String> project = new ArrayList<>();
                List<PyObject> pyArrayOfProjects = module.callAttr("obtain_suggestions", index, projectData.toArray()).asList();
                List<PyObject> thisProjectList= pyArrayOfProjects.get(0).asList();

                for (PyObject item : thisProjectList) {
                    project.add(item.toString());
                }

                projects.add(project);
            }

            //Collections.shuffle(projects);
            return projects;

        }


        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> results) {
            super.onPostExecute(results);
            // do something with result

            LoadingSuggestions(false);

            FragmentTransaction presentProjectSuggestions = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.recommenderContainterView,
                            new ProjectSuggestions(results.get(0),
                                    results.get(1),
                                    results.get(2),
                                    results.get(3),
                                    results.get(4),
                                    results.get(5))).addToBackStack("suggestions");
            presentProjectSuggestions.commit();

        }
    }

    public void LoadingSuggestions(Boolean load) {
        ProgressBar progress = findViewById(R.id.progressBar_cyclic);
        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.recommender_activity);
        if (load) {
            progress.setVisibility(View.VISIBLE);
            cl.setBackgroundColor(Color.WHITE);
            cl.setClickable(true);
        } else {
            progress.setVisibility(View.GONE);
            cl.setBackgroundColor(0x00000000); // transparent
            cl.setClickable(false);

        }
    }
}
