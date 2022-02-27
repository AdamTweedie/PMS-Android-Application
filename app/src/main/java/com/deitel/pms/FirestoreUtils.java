package com.deitel.pms;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtils {

    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    final User user = new User();
    final String TAG = "FireStore Process: ";
    final String USER_COLLECTION_PATH = "users";
    final String FIELD_SUPERVISOR_EMAIL = "supervisor email";
    final String FIELD_PROJECT_TITLE = "project title";
    final String FIELD_PROJECT_DESCRIPTION = "project description";
    final String FIELD_PROJECT_APPROVED = "approved project";


    public void addUserProject(Activity activity,
                               String supervisorEmail,
                               String supervisorName,
                               String projectTitle,
                               String projectDescription) {

        String userId = user.getUserId(activity);

        Map<String, Object> project = new HashMap<>();
        project.put("supervisor email", supervisorEmail);
        project.put("supervisor name", supervisorName);
        project.put("project title", projectTitle);
        project.put("project description", projectDescription);
        project.put("approved project", "false");

        dbInstance.collection(USER_COLLECTION_PATH)
                .document(userId)
                .set(project, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + userId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public void createAccount() {

    }

    public void setProjectApproval() {

    }

    public String getUSER_COLLECTION_PATH() {
        return USER_COLLECTION_PATH;
    }

    public String getFIELD_SUPERVISOR_EMAIL() {
        return FIELD_SUPERVISOR_EMAIL;
    }

    public String getFIELD_PROJECT_TITLE() {
        return FIELD_PROJECT_TITLE;
    }

    public String getFIELD_PROJECT_DESCRIPTION() {
        return FIELD_PROJECT_DESCRIPTION;
    }

    public String getFIELD_PROJECT_APPROVED() {
        return FIELD_PROJECT_APPROVED;
    }
}
