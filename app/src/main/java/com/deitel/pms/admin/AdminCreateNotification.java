package com.deitel.pms.admin;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.ActiveUniAdmin;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminCreateNotification extends Fragment {

        private final String user;
        private final String collection;
        AdminCreateNotification(String userId, String collectionId) {
            this.user = userId;
            this.collection = collectionId;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.supervisor_create_notification, container, false);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            EditText etTitle = (EditText) view.findViewById(R.id.scnEtTitle);
            EditText etDescription = (EditText) view.findViewById(R.id.scnEtDescription);
            EditText etDueDate = (EditText) view.findViewById(R.id.scnEtDueDate);
            Button btnSubmitNotification = (Button) view.findViewById(R.id.btnSubmitNotification);
            ImageButton btnUndo = (ImageButton) view.findViewById(R.id.scnBtnUndo);

            btnSubmitNotification.setOnClickListener(view1 -> {
                final String title = etTitle.getText().toString();
                final String description = etDescription.getText().toString();
                final String dueDate = etDueDate.getText().toString();

                if (title.length()>10) {
                    sendNotification(this.collection, this.user, title, description, dueDate);
                    getParentFragmentManager().popBackStack();
                    Toast.makeText(getContext(), "Notification sent", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getContext(), "Title insufficient length", Toast.LENGTH_SHORT).show();
                }
            });

            btnUndo.setOnClickListener(view12 -> getParentFragmentManager().popBackStack());

        }

        public void sendNotification(String collectionId, String documentId, String title, String description, String dueDate) {
            // TODO - make sure this works
            User user = new User();
            String senderEmail = user.getUserId(requireActivity()).split("-"+ ActiveUniAdmin.EXETER_ADMIN.getValue())[0];

            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("title", title);
            notificationData.put("description", description);
            notificationData.put("sender", senderEmail);
            notificationData.put("due date", dueDate);
            FirebaseFirestore.getInstance()
                    .collection(collectionId)
                    .document(documentId)
                    .collection("notifications")
                    .add(notificationData)
                    .addOnSuccessListener(unused -> Log.w("LOGGER", "Successfully sent notification to " + documentId))
                    .addOnFailureListener(e -> Log.e("LOGGER", "failed to send notification: " + e));

        }
}
