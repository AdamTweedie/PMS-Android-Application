package com.deitel.pms.supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateNotification extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_create_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etTitle = (EditText) view.findViewById(R.id.scnEtTitle);
        EditText etDescription = (EditText) view.findViewById(R.id.scnEtDescription);
        EditText etDueDate = (EditText) view.findViewById(R.id.scnEtDueDate);
        Button btnSubmitNotification = (Button) view.findViewById(R.id.btnSubmitNotification);
        ImageButton btnUndo = (ImageButton) view.findViewById(R.id.scnBtnUndo);

        User user = new User();

        btnSubmitNotification.setOnClickListener(view1 -> {
            final String title = etTitle.getText().toString();
            final String description = etDescription.getText().toString();
            final String dueDate = etDueDate.getText().toString();

            if (title.length()>10) {
                submitNotification(user.getUserId(requireActivity()), title, description, dueDate);
            } else {
                Toast.makeText(getContext(), "Title insufficient length", Toast.LENGTH_SHORT).show();
            }
        });

        btnUndo.setOnClickListener(view12 -> getParentFragmentManager().popBackStack());

    }

    private void submitNotification(String sender, String title, String description, String dueDate) {

        FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        FirestoreUtils utils = new FirestoreUtils();

        Map<String, Object> notification = new HashMap<>();
        notification.put("sender", sender);
        notification.put("title", title);
        notification.put("description", description);
        notification.put("due date", dueDate);

        dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                .document(sender)
                .collection("created notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> {
                    Log.w("LOGGER", "successfully added notification");
                    Toast.makeText(requireContext(), "Added notification", Toast.LENGTH_SHORT).show();
                    // add notification to students
                    dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                            .document(sender)
                            .collection("approved projects")
                            .get()
                            .addOnCompleteListener(task -> {
                                QuerySnapshot snapshot = task.getResult();
                                for (QueryDocumentSnapshot document : snapshot) {
                                    dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                                            .document(document.getId())
                                            .collection("notifications")
                                            .add(notification)
                                            .addOnSuccessListener(documentReference1 -> {
                                                Log.w("LOGGER", "Added notification to students notifications");
                                                try {
                                                    getParentFragmentManager().popBackStack();
                                                } catch (Exception e) {
                                                    Log.e("LOGGER", "failed with exception: " + e);
                                                }
                                            }).addOnFailureListener(e -> Log.w("LOGGER", "failed to add notification to students notifications"));
                                }
                            }).addOnFailureListener(e -> Log.w("LOGGER", "failed to access my students"));
                }).addOnFailureListener(e -> {
                    Log.w("LOGGER", "failed to add new notification");
                    Toast.makeText(requireContext(), "Failed to add notification",
                            Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                });

    }
}
