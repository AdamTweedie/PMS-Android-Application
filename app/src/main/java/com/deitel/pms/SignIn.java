package com.deitel.pms;

import com.deitel.pms.student.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deitel.pms.student.HomeActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SignIn extends Fragment {

    private Button btnSignUp;
    private Button btnSignIn;
    private Button btnResetPassword;
    private EditText email;
    private EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        btnResetPassword = (Button) view.findViewById(R.id.btnResetPassword);
        email = (EditText) view.findViewById(R.id.etUserEmail);
        password = (EditText) view.findViewById(R.id.etUserPassword);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // todo - lots to do here
//                final String userEmail = email.getText().toString();
//                final String userPassword = password.getText().toString();

//                FirestoreUtils firestoreUtils = new FirestoreUtils();
//                FirebaseFirestore db = firestoreUtils.getDb();
//                // Create a reference to the cities collection
//                CollectionReference users = db.collection("Users");
//                // Create a query against the collection
//                Query query = users.whereEqualTo("email", userEmail);
//                // retrieve query results asynchronously using query.get()
//                Task<QuerySnapshot> querySnapshot = query.get();
//
//                for (DocumentSnapshot document : querySnapshot.getResult().getDocuments()) {
//                    System.out.println(document.getId());
//                }

                // if credentials okay ---
                Intent in = new Intent(getActivity(), HomeActivity.class);
                getActivity().finish();
                startActivity(in);


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Success!!");

                final FragmentTransaction fragmentTransaction;
                final FragmentManager fragmentManager = getParentFragmentManager();

                final Fragment fragment = getParentFragmentManager()
                        .findFragmentById(R.id.placeholder_main);

                if (fragment!=null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    //fragmentTransaction.commit();
                    fragmentTransaction.add(R.id.placeholder_main, new SignUp());
                    fragmentTransaction.commit();
                }
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            // TODO - add functionality to this
            @Override
            public void onClick(View view) {
                // Change button color onclick
                ButtonUtils.textButtonColorChange(btnResetPassword);
            }
        });
    }
}
