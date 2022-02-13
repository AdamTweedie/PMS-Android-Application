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

import com.deitel.pms.student.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends Fragment {

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

                Map<String, Object> user = new HashMap<>();


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
                } else if (ExistingUser(finalEmail)) {
                    Toast.makeText(getContext(), "Email already in use", Toast.LENGTH_SHORT).show();
                    email.getText().clear();
                } else if (!ValidUniAccessCode(finalUniAccessCode)) {
                    Toast.makeText(getContext(), "Incorrect Uni Access Code", Toast.LENGTH_SHORT).show();
                    uniAccessCode.getText().clear();
                } else {

                    // Create user info
                    user.put("email", finalEmail);
                    user.put("uni id", finalUniAccessCode);

                    String TAG = "New User Creation:";


                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                    SharedPrefUtils utils = new SharedPrefUtils();

                    MasterKey masterKeyAlias = utils.getMasterKey(context);
                    SharedPreferences encryptedPreferences = utils.getEncryptedPreferences(masterKeyAlias, context);
                    if (masterKeyAlias!=null && encryptedPreferences!=null) {
                        SharedPreferences.Editor editor = encryptedPreferences.edit();
                        editor.putString(finalEmail + finalPassword + "data", "Signed in as " + finalEmail);
                        editor.apply();

                        Intent homeScreen = new Intent(getActivity(), HomeActivity.class);
                        // Load MainActivity via implicit intent
                        startActivity(homeScreen);
                        getActivity().finish();
                        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show();
                    }

                }
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


    private boolean ValidUniAccessCode(String finalUniAccessCode) {
        // TODO - add functionality
        return true;
    }

    private boolean ExistingUser(String finalEmail) {
        // TODO - add functionality
        return false;
    }

    private boolean ValidEmail(String finalEmail) {
        // TODO - add functionality
        return true;
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
