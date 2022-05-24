package com.deitel.pms.student;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Notifications extends Fragment implements NotificationRecyclerViewAdapter.ItemClickListener {

    NotificationRecyclerViewAdapter adapter;
    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    User user = new User();
    ArrayList<ArrayList<String>> notifications = new ArrayList<>();

    private final String collection;
    private final int fragmentId;
    public Notifications(String collectionPath, int fragmentId) {
        this.collection = collectionPath;
        this.fragmentId = fragmentId;
    }

    boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new NotificationRecyclerViewAdapter(context, notifications);

        dbInstance.collection(this.collection)
                .document(user.getUserId(requireActivity()))
                .collection("notifications")
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot snapshots = task.getResult();
                    for (QueryDocumentSnapshot notification : snapshots) {
                        ArrayList<String> notificationData = new ArrayList<>();
                        notificationData.add((String) notification.get("title"));
                        notificationData.add((String) notification.get("description"));
                        notificationData.add((String) notification.get("sender"));
                        notificationData.add((String) notification.get("due date"));
                        notificationData.add((String) notification.getId());

                        notifications.add(notificationData);
                    }
                    adapter.setmData(notifications);
                    adapter.setClickListener(Notifications.this);
                    recyclerView.setAdapter(adapter);
                }).addOnFailureListener(e -> {

                });

    }

    public void removeSingleItem(int removeIndex) {
        notifications.remove(removeIndex);
        adapter.notifyItemRemoved(removeIndex);
    }

    @Override
    public void onItemClick(View view, int position) {

        if (getFlag()) {
            ArrayList<String> notification = adapter.getItem(position);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(this.fragmentId, new ExpandedNotification(notification.get(0),
                            notification.get(1),
                            notification.get(2),
                            notification.get(3),
                            notification.get(4),
                            Notifications.this,
                            position,
                            getCollection(),
                            getFragmentId()))
                    .addToBackStack("expanded notification")
                    .commit();
            setFlag(!flag);
        }
    }

    private String getCollection() {
        return this.collection;
    }

    private int getFragmentId() {
        return this.fragmentId;
    }
    public void setFlag(boolean b) {
        this.flag = b;
    }

    public boolean getFlag() {
        return this.flag;
    }
}

