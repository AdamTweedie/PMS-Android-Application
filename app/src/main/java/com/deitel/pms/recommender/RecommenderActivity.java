package com.deitel.pms.recommender;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.deitel.pms.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecommenderActivity extends AppCompatActivity {

    RecommenderSetup recommenderSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        FragmentTransaction signInFragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recommenderContainterView, new ProjectSelectionIntro()).addToBackStack("intro");
        signInFragmentTransaction.commit();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported

//
//                recommenderSetup = new RecommenderSetup("Exeter", "CompSci", result);
//                FirebaseStorage storage = recommenderSetup.getStorage();
//                StorageReference reference = storage.getReferenceFromUrl("gs://pms-project-300122.appspot.com/Project Data/Exeter/CompSci/Exe-CompSci-Unclean.csv");
//
//                StorageReference fileRef = storage.getReference()
//                        .child("Project Data")
//                        .child("Exeter")
//                        .child("CompSci")
//                        .child("ExeCompSciDataClean.csv");
//
//                String uri = fileRef.getDownloadUrl().toString();
//                System.out.println("URL - " + uri);
//
//                fileRef.getBytes(1024*1024)
//                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//
//                        String s = new String(bytes, StandardCharsets.UTF_8);
//
//                    }
//                });

                String result = bundle.getString("bundleKey");
                System.out.println("this is the result in activity " + result);

                // String cleanEntry = sortResult(result)

                new MyTask().execute(result);
                LoadingSuggestions(true);

            }
        });
    }


    private class MyTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... params) {

            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(getApplicationContext()));
            }
            Python py = Python.getInstance();
            PyObject module = py.getModule("Recommender");

            List<PyObject> pyIndexList = module.callAttr("recommender", params[0]).asList();

            ArrayList<Integer> indexList = new ArrayList<>();
            ArrayList<ArrayList<String>> projects = new ArrayList<>();

            for (PyObject pyObj : pyIndexList) {
                indexList.add(pyObj.hashCode());
            }
            Collections.shuffle(indexList);

            if (indexList.size()>5) {
                int t1=indexList.get(0), t2=indexList.get(1), t3=indexList.get(2),
                        t4=indexList.get(3), t5=indexList.get(4);

                indexList.clear();
                indexList.add(t1);
                indexList.add(t2);
                indexList.add(t3);
                indexList.add(t4);
                indexList.add(t5);
            } // TODO - if not then add a few more projects from neighboring clusters


            for (int index : indexList) {
                ArrayList<String> project = new ArrayList<>();
                List<PyObject> pyArrayOfProjects = module.callAttr("obtain_suggestions", index).asList();
                List<PyObject> thisProjectList= pyArrayOfProjects.get(0).asList();

                for (PyObject item : thisProjectList) {
                    project.add(item.toString());
                }

                projects.add(project);
            }

            Collections.shuffle(projects);
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
                                    results.get(3))).addToBackStack("suggestions");
            presentProjectSuggestions.commit();

            // TODO - FILTER SPECIFIC INFORMATION FROM ARRAYLIST AND ADD TO UI


        }
    }

    public void LoadingSuggestions(Boolean load) {
        ProgressBar progress = findViewById(R.id.progressBar_cyclic);

        if (load) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
        }
    }
}
