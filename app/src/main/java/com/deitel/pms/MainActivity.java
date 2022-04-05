package com.deitel.pms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.deitel.pms.recommender.RecommenderActivity;
import com.deitel.pms.student.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Begin fragment transaction to show Sign-In page
        FragmentTransaction signInFragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.placeholder_main, new SignIn());
        signInFragmentTransaction.commit();

    }
}