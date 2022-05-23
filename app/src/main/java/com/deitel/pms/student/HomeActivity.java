package com.deitel.pms.student;
import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.messaging.MessageCenter;
import com.deitel.pms.student.kanban.KanbanBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    FirestoreUtils utils = new FirestoreUtils();
    User user = new User();

    // TODO - add functionality to the nav bar buttons so they change color on click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navbar);

        final Button btnWorkspace = findViewById(R.id.btnNavWorkspace);
        final Button btnNotifications = findViewById(R.id.btnNavNotifications);
        final Button btnSupervisor = findViewById(R.id.btnNavSupervisor);
        final Button btnProfile = findViewById(R.id.btnNavProfile);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fm.getFragments().isEmpty()) {
            fragmentTransaction.add(R.id.nav_bar_fragment, new Workspace()).commit();
        }

        btnWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());
                fm.beginTransaction()
                        .add(R.id.nav_bar_fragment, new Workspace())
                        .addToBackStack("workspace")
                        .commit();
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());
                fm.beginTransaction()
                        .add(R.id.nav_bar_fragment,
                                new Notifications("users", R.id.nav_bar_fragment))
                        .addToBackStack("notifications")
                        .commit();
            }
        });

        btnSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());

                dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                        .document(user.getUserId(HomeActivity.this))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (Objects.equals(task.getResult().get("approved project"), true)) {
                                    fm.beginTransaction()
                                            .add(R.id.nav_bar_fragment,
                                                    new MessageCenter((String) task.getResult().get("supervisor email"),
                                                            user.getUserId(HomeActivity.this),
                                                            getApplicationContext()))
                                            .addToBackStack("notifications")
                                            .commit();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGGER", "Cannot load messages with exception " + e);
                        Toast.makeText(getApplicationContext(), "cannot access feature yet", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBackStack(fm.getBackStackEntryCount());
                fm.beginTransaction()
                        .add(R.id.nav_bar_fragment, new Profile())
                        .addToBackStack("profile")
                        .commit();
            }
        });
    }

    private void clearBackStack(int stackCount) {
        for (int i = 0; i < stackCount; ++i) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
