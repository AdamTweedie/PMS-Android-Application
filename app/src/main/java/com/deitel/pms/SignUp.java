package com.deitel.pms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.MasterKey;

import com.deitel.pms.recommender.RecommenderActivity;
import com.deitel.pms.student.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends Fragment {

    // TODO - configure this so it will still run without a connection to the internet.

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button btnAlreadyMember;
    private Button btnSignUp;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText uniAccessCode;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO - add functionality
        btnAlreadyMember = (Button) view.findViewById(R.id.btnBackToSignIn);
        btnSignUp = (Button) view.findViewById(R.id.btnCreateAccount);
        email = (EditText) view.findViewById(R.id.etNewEmail);
        password = (EditText) view.findViewById(R.id.etNewPassword);
        confirmPassword = (EditText) view.findViewById(R.id.etNewConfirmPassword);
        uniAccessCode = (EditText) view.findViewById(R.id.etUniversityAccessCode);
        Context context = getContext();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String finalEmail = email.getText().toString();
                final String finalPassword = password.getText().toString();
                final String finalConfirmPassword = confirmPassword.getText().toString();
                final String finalUniAccessCode = uniAccessCode.getText().toString();
                final String TAG = "Sign-Up";

                if (finalEmail.isEmpty() ||
                        finalPassword.isEmpty() ||
                        finalConfirmPassword.isEmpty() ||
                        finalUniAccessCode.isEmpty()) {
                    Toast.makeText(getContext(), "Missing values !", Toast.LENGTH_SHORT).show();
                } else if (!finalPassword.equals(finalConfirmPassword)) {
                    Toast.makeText(getContext(), "Passwords do not match !", Toast.LENGTH_SHORT).show();
                    password.getText().clear();
                    confirmPassword.getText().clear();
                } else if (!ValidEmail(finalEmail)) {
                    Toast.makeText(getContext(), "Invalid email !", Toast.LENGTH_SHORT).show();
                    email.getText().clear();
                } else if (!ValidUniAccessCode(finalUniAccessCode)) {
                    Toast.makeText(getContext(), "Incorrect Uni Access Code", Toast.LENGTH_SHORT).show();
                    uniAccessCode.getText().clear();
                } else {
                    DocumentReference docRef = db.collection("users").document(finalEmail);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                // if user does exist
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getId() + " exists already");
                                    Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show();
                                    email.setText("");
                                    password.setText("");
                                    confirmPassword.setText("");
                                    uniAccessCode.setText("");
                                } else {
                                    Log.d(TAG, "No such document");
                                    createAccount(finalEmail, finalUniAccessCode, TAG);
                                    saveToPrefs(finalEmail, finalPassword, context);
                                    loadRecommenderSystem(context);
                                }

                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }

                // Does user already exist....

            }
        });

        btnAlreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change button color onClick
                ButtonUtils.textButtonColorChange(btnAlreadyMember);
                replaceFragment(new SignIn());
            }
        });
    }

    // TODO - NEED TO ADD DIFFERENT CREDENTIAL CHECKER FOR SUPERVISORS
    private boolean ValidUniAccessCode(String finalUniAccessCode) {
        for (ActiveUnis uni : ActiveUnis.values()) {
            try {
                if (uni.getValue() == Integer.parseInt(finalUniAccessCode)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean ValidEmail(String finalEmail) {
        String[] parts = finalEmail.split("@");
        for (ActiveUnis uni : ActiveUnis.values()) {
            if (uni.getKey().equals(parts[1])) {
                return true;
            }
        }
        return false;
    }


    public void createAccount(String email, String uniCode, String TAG) {
        // Create user info

        // TODO - clear shared prefs first
        User newUser = new User();
        newUser.setUserId(requireActivity(), email);


        Map<String, Object> user = new HashMap<>();
        user.put("uni id", uniCode);

        db.collection("users")
                .document(email)
                .set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + email);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public void saveToPrefs(String email, String password, Context context) {
        SharedPrefUtils utils = new SharedPrefUtils();
        MasterKey masterKeyAlias = utils.getMasterKey(context);
        SharedPreferences encryptedPreferences = utils.getEncryptedPreferences(masterKeyAlias, context);

        if (masterKeyAlias != null && encryptedPreferences != null) {
            SharedPreferences.Editor editor = encryptedPreferences.edit();
            editor.putString(email + password + "data", "Signed in as " + email);
            editor.apply();
        }
    }

    public void loadRecommenderSystem(Context context) {
        Intent recommender = new Intent(getActivity(), RecommenderActivity.class);
        startActivity(recommender);
        getActivity().finish();
        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show();
    }


    public void replaceFragment(Object object) {
        final FragmentTransaction fragmentTransaction;
        final FragmentManager fragmentManager = getParentFragmentManager();

        final Fragment fragment = getParentFragmentManager()
                .findFragmentById(R.id.placeholder_main);

        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.add(R.id.placeholder_main, (Fragment) object);
            fragmentTransaction.commit();
        }
    }
}
