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
    private final String collection;
    private final String nId;
    private final Notifications notifications;
    private final int itemPosition;
    private final int fragmentId;
    ExpandedNotification(String title, String description, String dueDate, String sender,
                         String id, Notifications notif, int position, String collectionPath,
                         int fragmentId) {
        this.nTitle = title;
        this.nDescription = description;
        this.nDueDate = dueDate;
        this.nSender = sender;
        this.nId = id;
        this.notifications = notif;
        this.itemPosition = position;
        this.collection = collectionPath;
        this.fragmentId = fragmentId;
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

        undo.setOnClickListener(view12 -> {
            notifications.setFlag(true);
            getParentFragmentManager().popBackStack();
            try {
                Fragment fragment = getParentFragmentManager().findFragmentById(this.fragmentId);
                getParentFragmentManager().beginTransaction().detach(fragment).attach(fragment);
            } catch (Exception ignored) {}
        });

        deleteNotification.setOnClickListener(view1 -> {
            // delete notification
            dbInstance.collection(getCollection())
                    .document(user.getUserId(requireActivity()))
                    .collection("notifications")
                    .document(getnId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Log.w("LOGGER", "Successfully deleted notification with id: " + getnId());
                        Toast.makeText(getContext(), "Successfully deleted notification", Toast.LENGTH_SHORT).show();

                        getParentFragmentManager().popBackStack();
                        Fragment fragment = getParentFragmentManager().findFragmentById(getFragmentId());
                        assert fragment != null;
                        notifications.removeSingleItem(itemPosition);
                    }).addOnFailureListener(e -> {
                        Log.w("LOGGER", "Failed to delete notification with id: " + getnId());
                        Toast.makeText(getContext(), "Failed to delete notification", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private String getCollection() {
        return this.collection;
    }

    private int getFragmentId() {
        return this.fragmentId;
    }

    private String getnId() {
        return this.nId;
    }
}
