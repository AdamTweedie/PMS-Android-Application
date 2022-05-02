package com.deitel.pms.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.deitel.pms.SignIn;
import com.deitel.pms.User;
import com.deitel.pms.recommender.RecommenderActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Profile extends Fragment {

    User user = new User();
    FirestoreUtils u = new FirestoreUtils();
    final FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String userId = user.getUserId(requireActivity());
        TextView userEmail = (TextView) view.findViewById(R.id.tvProfileUserEmail);
        TextView userSupervisor = (TextView) view.findViewById(R.id.tvProfileUserSupervisor);
        final Button signOut = (Button) view.findViewById(R.id.btnAccountSignOut);
        final Context context = getContext();

        userEmail.setText(userId);

        DocumentReference docRef = dbInstance.collection(u.getUSER_COLLECTION_PATH()).document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                try {
                    userSupervisor.setText(document.getString(u.getFIELD_SUPERVISOR_EMAIL()));
                } catch (Exception e) {
                    Log.e("LOGGER", "failed with exception " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("LOGGER", "get failed with " + e);
                Toast.makeText(context, "No connection to database !", Toast.LENGTH_SHORT).show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unsaveUserCredentials(requireActivity());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
                Toast.makeText(context, "Goodbye!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    public void unsaveUserCredentials(Activity activity) {
        SharedPreferences sharedPreferences = (SharedPreferences) activity
                .getSharedPreferences("save_creds", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rememberMe", "no");
        editor.apply();
    }
}
