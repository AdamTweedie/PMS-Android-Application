package com.deitel.pms.recommender;

import android.os.AsyncTask;
import android.os.Bundle;

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
import java.util.List;

public class RecommenderActivity extends AppCompatActivity {

    RecommenderSetup recommenderSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        FragmentTransaction signInFragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recommenderContainterView, new SubjectInterestSpinner()).addToBackStack("pickInterests");
        signInFragmentTransaction.commit();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                System.out.println("this is the result in activity " + result);

                recommenderSetup = new RecommenderSetup("Exeter", "CompSci", result);
                FirebaseStorage storage = recommenderSetup.getStorage();
                StorageReference reference = storage.getReferenceFromUrl("gs://pms-project-300122.appspot.com/Project Data/Exeter/CompSci/Exe-CompSci-Unclean.csv");

                StorageReference fileRef = storage.getReference()
                        .child("Project Data")
                        .child("Exeter")
                        .child("CompSci")
                        .child("ExeCompSciDataClean.csv");

                String uri = fileRef.getDownloadUrl().toString();
                System.out.println("URL - " + uri);

                fileRef.getBytes(1024*1024)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        String s = new String(bytes, StandardCharsets.UTF_8);

                    }
                });

                new MyTask().execute(result);

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
            //int[] project_index = module.callAttr("recommender", params[0]).asList();

            List<PyObject> pyIndexList = module.callAttr("recommender", params[0]).asList();

            ArrayList<Integer> indexList = new ArrayList<>();
            ArrayList<ArrayList<String>> projects = new ArrayList<>();

            for (PyObject pyObj : pyIndexList) {
                indexList.add(pyObj.hashCode());
            }

            System.out.println(indexList);

            ArrayList<String> project = new ArrayList<>();
            for (int index : indexList) {
                List<PyObject> pyProject = module.callAttr("obtain_suggestions", index).asList();
                for (PyObject item : pyProject) {
                    project.add(item.toString());
                }
                projects.add(project);
            }

            System.out.println(projects);
            System.out.println(projects.size());

            return projects;

        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> results) {
            super.onPostExecute(results);
            // do something with result

            // TODO - FILTER SPECIFIC INFORMATION FROM ARRAYLIST AND ADD TO UI
            // show results in UI
        }
    }
}
