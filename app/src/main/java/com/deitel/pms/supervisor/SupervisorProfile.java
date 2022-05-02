package com.deitel.pms.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.MainActivity;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.student.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SupervisorProfile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        final FirestoreUtils u = new FirestoreUtils();
        final Profile profile = new Profile();
        final User user = new User();
        final String userId = user.getUserId(requireActivity());

        final TextView sEmail = (TextView) view.findViewById(R.id.spTvEmail);
        sEmail.setText(userId);
        final TextView sGroupSize = (TextView) view.findViewById(R.id.spTvGroupSize);
        final TextView sModuleLead = (TextView) view.findViewById(R.id.spTvModuleLead);
        final TextView sMaintenance = (TextView) view.findViewById(R.id.spTvMaintenance);

        final Button btnSignOut = (Button) view.findViewById(R.id.btnSupervisorAccountSignOut);
        final Button btnDeleteAccount = (Button) view.findViewById(R.id.btnSupervisorAccountDeleteAccount);


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.unsaveUserCredentials(requireActivity());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
                Toast.makeText(getContext(), "Goodbye!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
            }
        });


        dbInstance.collection(u.getSUPERVISOR_COLLECTION_PATH())
                .document(userId)
                .collection("approved projects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int count = 0;
                for (int i = 0; i < task.getResult().size(); i++) {
                    count = count + 1;
                }
                sGroupSize.setText(String.valueOf(count));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sGroupSize.setText("0");
            }
        });
    }
}
