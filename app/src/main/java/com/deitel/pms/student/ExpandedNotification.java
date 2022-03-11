package com.deitel.pms.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class ExpandedNotification extends Fragment {


    private final String nTitle;
    private final String nDescription;
    private final String nDueDate;
    private final String nSender;
    private final String nId;
    private final Notifications notifications;

    ExpandedNotification(String title, String description, String dueDate, String sender, String id, Notifications notif) {
        this.nTitle = title;
        this.nDescription = description;
        this.nDueDate = dueDate;
        this.nSender = sender;
        this.nId = id;
        this.notifications = notif;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expanded_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        final FirestoreUtils utils = new FirestoreUtils();
        final User user = new User();

        TextView tvTitle = (TextView) view.findViewById(R.id.expandedNotificationTitle);
        TextView tvDescription = (TextView) view.findViewById(R.id.expandedNotificationDescription);
        TextView tvSender = (TextView) view.findViewById(R.id.expandedNotificationSenderId);
        TextView tvDueDate = (TextView) view.findViewById(R.id.expandedNotificationDueDate);

        ImageButton undo = (ImageButton) view.findViewById(R.id.btnMinimiseNotification);
        Button deleteNotification = (Button) view.findViewById(R.id.btnDeleteNotification);

        tvTitle.setText(this.nTitle);
        tvDescription.setText(this.nDescription);
        tvSender.setText(this.nSender);
        tvDueDate.setText(this.nDueDate);


        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications.setFlag(true);
                getParentFragmentManager().popBackStack();
                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.nav_bar_fragment);
                assert fragment != null;
                getParentFragmentManager().beginTransaction().detach(fragment).attach(fragment);
            }
        });

        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete notification

                dbInstance.collection(utils.getUSER_COLLECTION_PATH())
                        .document(user.getUserId(requireActivity()))
                        .collection("notifications")
                        .document(getnId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.w("LOGGER", "Successfully deleted notification with id: " + getnId());
                                Toast.makeText(getContext(), "Successfully deleted notification", Toast.LENGTH_SHORT).show();

                                getParentFragmentManager().popBackStack();
                                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.nav_bar_fragment);
                                assert fragment != null;
                                getParentFragmentManager().beginTransaction().detach(fragment);
                                getParentFragmentManager().beginTransaction().attach(fragment);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGGER", "Failed to delete notification with id: " + getnId());
                        Toast.makeText(getContext(), "Failed to delete notification", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getnId() {
        return this.nId;
    }
}
