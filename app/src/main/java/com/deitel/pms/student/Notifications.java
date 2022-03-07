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

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.supervisor.ExpandedProjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Notifications extends Fragment implements NotificationRecyclerViewAdapter.ItemClickListener {

    NotificationRecyclerViewAdapter adapter;
    FirestoreUtils u = new FirestoreUtils();
    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    User user = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO - add better functionality,
        // TODO - calls to database
        // TODO - check this fragment runs correctly
        ArrayList<ArrayList<String>> notifications = new ArrayList<>();

        Context context = getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new NotificationRecyclerViewAdapter(context, notifications);


        dbInstance.collection(u.getUSER_COLLECTION_PATH())
                .document(user.getUserId(requireActivity()))
                .collection("notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        ArrayList<String> notification = adapter.getItem(position);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_bar_fragment, new ExpandedNotification(notification.get(0),
                        notification.get(1),
                        notification.get(2),
                        notification.get(3),
                        notification.get(4)))
                .addToBackStack("expanded notification")
                .commit();
    }
}

