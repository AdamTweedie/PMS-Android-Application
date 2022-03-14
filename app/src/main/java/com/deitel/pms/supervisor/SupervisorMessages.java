package com.deitel.pms.supervisor;

import android.os.Bundle;
import android.util.Log;
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
import com.deitel.pms.messaging.MessageCenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SupervisorMessages extends Fragment implements MyStudentsRecyclerViewAdapter.ItemClickListener{

    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    User user = new User();
    FirestoreUtils utils = new FirestoreUtils();
    MyStudentsRecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_message_frag_holder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.supervisorRvContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyStudentsRecyclerViewAdapter(getContext(), null);
        dbInstance.collection(utils.getSUPERVISOR_COLLECTION_PATH())
                .document(user.getUserId(requireActivity()))
                .collection("approved projects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshots = task.getResult();
                        ArrayList<String> myStudentArray = new ArrayList<>();
                        for (QueryDocumentSnapshot student : snapshots) {
                            myStudentArray.add(student.getId());
                        }
                        System.out.println("My Students " + myStudentArray);
                        adapter.setmData(myStudentArray);
                        adapter.setClickListener(SupervisorMessages.this);
                        recyclerView.setAdapter(adapter);
                        Log.w("LOGGER", "successfully loaded myStudents to RecyclerView");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("LOGGER", "failed to upload myStudents to RecyclerView");
            }
        });

    }
    @Override
    public void onItemClick(View view, int position) {

        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position), Toast.LENGTH_SHORT).show();

        getChildFragmentManager().popBackStack();
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragmentForSupervisorMessages,
                        new MessageCenter(adapter.getItem(position),
                                user.getUserId(requireActivity()),
                                getContext())).addToBackStack("messages mite")
                .commit();


    }
}
