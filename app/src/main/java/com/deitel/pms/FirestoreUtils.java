package com.deitel.pms;

import static java.util.Objects.requireNonNull;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirestoreUtils {

    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    final User user = new User();
    final String TAG = "FireStore Process: ";

    // student paths
    final String USER_COLLECTION_PATH = "users";
    final String FIELD_SUPERVISOR_EMAIL = "supervisor email";
    final String FIELD_PROJECT_TITLE = "project title";
    final String FIELD_PROJECT_DESCRIPTION = "project description";
    final String FIELD_PROJECT_APPROVED = "approved project";

    // supervisor paths
    final String SUPERVISOR_COLLECTION_PATH = "supervisors";
    final String FIELD_SUPERVISOR_ACCOUNT_CREATED = "account created";
    final String SUPERVISOR_REQUESTS_COLLECTION_PATH = "project requests";
    final String SUPERVISOR_REQUESTS_PROJECT_TITLE_FIELD = "project title";
    final String SUPERVISOR_REQUESTS_PROJECT_DESCRIPTION_FIELD = "project description";


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
        project.put("approved project", false);

        dbInstance.collection(USER_COLLECTION_PATH)
                .document(userId)
                .update(project)
                .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot added with ID: " + userId))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void studentSuggestedProjectRequest(String userId, String projectTitle, String projectDescription) {
        Map<String, Object> projectRequestInfo = new HashMap<>();
        projectRequestInfo.put("project title", projectTitle);
        projectRequestInfo.put("project description", projectDescription);

        dbInstance.collection("student suggested projects")
                .document(userId)
                .set(projectRequestInfo)
                .addOnSuccessListener(unused -> Log.w(TAG, "Successfully added suggested project!"))
                .addOnFailureListener(e -> Log.w(TAG, "Failed to add suggested project!"));
    }

    public void standardProjectRequest(String userId, String supervisorId,
                                       String projectTitle, String projectDescription) {

        Map<String, Object> projectRequestInfo = new HashMap<>();
        projectRequestInfo.put("project title", projectTitle);
        projectRequestInfo.put("project description", projectDescription);

        dbInstance.collection(SUPERVISOR_COLLECTION_PATH)
                .document(supervisorId)
                .collection(SUPERVISOR_REQUESTS_COLLECTION_PATH)
                .document(userId)
                .set(projectRequestInfo)
                .addOnSuccessListener(unused -> Log.w(TAG, "Successfully requested project"))
                .addOnFailureListener(e -> Log.w(TAG, "Failed to request project"));
    }

    public void deleteProjectRequest(String supervisorId, String studentId) {
        System.out.println("DELETE PROJECT REQUEST:");
        System.out.println("C-" + SUPERVISOR_COLLECTION_PATH);
        System.out.println("D- " + supervisorId);
        System.out.println("C- " + SUPERVISOR_REQUESTS_COLLECTION_PATH);
        System.out.println("D- " + studentId);
        dbInstance.collection(SUPERVISOR_COLLECTION_PATH)
                .document(supervisorId)
                .collection(SUPERVISOR_REQUESTS_COLLECTION_PATH)
                .document(studentId)
                .delete()
                .addOnSuccessListener(unused -> Log.w(TAG, "Successfully deleted project request"))
                .addOnFailureListener(e -> Log.w(TAG, "Failed to delete project request due to exception: " + e));
    }

    public static void createNotification(String collection, String senderId, String recipientId, String title, String body) {
        final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        Map<String, Object> notification = new HashMap<>();
        notification.put("sender", senderId);
        notification.put("title", title);
        notification.put("description", body);
        dbInstance.collection(collection)
                .document(recipientId)
                .collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> Log.w("LOGGER", "Successfully added notification to recipient " + recipientId))
                .addOnFailureListener(e -> Log.w("LOGGER", "Failed to add notification with exception: " + e));
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

    public String getSUPERVISOR_COLLECTION_PATH() {
        return SUPERVISOR_COLLECTION_PATH;
    }

    public String getFIELD_SUPERVISOR_ACCOUNT_CREATED() {
        return FIELD_SUPERVISOR_ACCOUNT_CREATED;
    }

    public String getSUPERVISOR_REQUESTS_COLLECTION_PATH() {
        return SUPERVISOR_REQUESTS_COLLECTION_PATH;
    }

    public String getSUPERVISOR_REQUESTS_PROJECT_TITLE_FIELD() {
        return SUPERVISOR_REQUESTS_PROJECT_TITLE_FIELD;
    }

    public String getSUPERVISOR_REQUESTS_PROJECT_DESCRIPTION_FIELD() {
        return SUPERVISOR_REQUESTS_PROJECT_DESCRIPTION_FIELD;
    }
}
