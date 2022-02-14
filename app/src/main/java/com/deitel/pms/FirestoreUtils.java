package com.deitel.pms;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtils {

    private static final String TAG = "Log";
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Map newUser(String email, String pswrd, String uniCode) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", pswrd);
        user.put("uni code", uniCode);

        return user;
    }

    public void newDocument(String email, String pswrd, String uniCode) {
        db.collection("Users")
                .add(newUser(email, pswrd, uniCode))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public boolean existingDocument(String collectionPath, String id) {

        return false;
    }

    public FirebaseFirestore getDb() {
        return FirebaseFirestore.getInstance();
    }
}
