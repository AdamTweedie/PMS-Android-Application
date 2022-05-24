package com.deitel.pms;

import com.deitel.pms.admin.AdminMainActivity;
import com.deitel.pms.student.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.MasterKey;

import com.deitel.pms.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignIn extends Fragment {

    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

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

        Context context = getContext();
        User user = new User();
        if (detailsSaved() && user.getUserId(requireActivity()).contains("supervisor")) {
            loadSupervisorActivity(context);
        } else if (detailsSaved() && !user.getUserId(requireActivity()).contains("supervisor")){
            loadHomeActivity(context);
        }

        Button btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        Button btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        CheckBox rememberCredentials = (CheckBox) view.findViewById(R.id.cbRememberLoginCredentials);
        email = (EditText) view.findViewById(R.id.etUserEmail);
        password = (EditText) view.findViewById(R.id.etUserPassword);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userEmail = email.getText().toString();
                final String userPassword = password.getText().toString();
                final String errorMsg = "Username or Password is Incorrect";

                if (userEmail.length() == 0 && userPassword.length() == 0) {
                    Toast.makeText(getContext(), "Invalid details", Toast.LENGTH_SHORT);
                } else {
                    SignUp signUp = new SignUp();
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(task -> {
                                User currentUser = new User();
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LOGGER", "signInWithEmail:success");
                                    // todo - merge above code into this.......
                                    currentUser.clearIdPreferences(requireActivity());
                                    currentUser.setUserId(requireActivity(), userEmail);
                                    if (!validDetails(getContext(), userEmail, userPassword,
                                            "Enabling offline access!")) {
                                        // if this is a new device but valid account, save to prefs
                                        signUp.saveToPrefs(userEmail, userPassword, getContext());
                                    }
                                    
                                    checkAdminAndSignIn(currentUser, context, userEmail, rememberCredentials.isChecked());
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOGGER", "signInWithEmail:failure", task.getException());
                                    //validDetails(getContext(), userEmail, userPassword, "Authentication failed.");
                                    signInUserWithNoInternet(context, userEmail, userPassword,
                                            errorMsg, currentUser, rememberCredentials.isChecked());
                                }
                            });
                }
            }
        });

        btnSignUp.setOnClickListener(view1 -> {
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
        });

    }

    private void checkAdminAndSignIn(User currentUser, Context context, String userEmail, boolean rememberDetails) {

        // check is admin
        dbInstance.collection("admin")
                .document(userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().get("adminId")==null) {
                        signInUser(currentUser, context, userEmail, rememberDetails);
                    } else {
                        if (task.getResult().get("adminId").toString().equals(ActiveUniAdmin.EXETER_ADMIN.getValue().toString())) {
                            System.out.println(task.getResult().get("adminId").toString());
                            currentUser.clearIdPreferences(requireActivity());
                            currentUser.setUserId(requireActivity(), userEmail+"-"+ActiveUniAdmin.EXETER_ADMIN.getValue());
                            loadAdminActivity(context);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    //signInUser(currentUser, context, userEmail, rememberDetails);

                    Toast.makeText(context, "Failed to log in.", Toast.LENGTH_SHORT).show();
                });
    }
    
    public void signInUser(User currentUser, Context context, String userEmail, boolean rememberDetails) {
        if (currentUser.getUserId(requireActivity()).contains("supervisor")) { // and is a supervisor account
            if (rememberDetails) {
                saveUserCredentials();
            }
            loadSupervisorActivity(context);
            System.out.println("Logged in with ID - " + currentUser.getUserId(requireActivity()));
        } else {
            currentUser.clearIdPreferences(requireActivity());
            currentUser.setUserId(requireActivity(), userEmail);
            if (rememberDetails) {
                saveUserCredentials();
            }
            System.out.println("Logged in with ID - " + currentUser.getUserId(requireActivity()));
            loadHomeActivity(context);
        }
    }

    private void signInUserWithNoInternet(Context context, String userEmail, String userPassword,
                                          String errorMsg, User user, boolean rememberDetails) {
        if (!validDetails(context, userEmail, userPassword, errorMsg)) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();

        } else if (validDetails(context, userEmail, userPassword, errorMsg)) {
            user.clearIdPreferences(requireActivity());
            user.setUserId(requireActivity(), userEmail);
            signInUser(user, context, userEmail, rememberDetails);
        }
    }

    private void saveUserCredentials() {
        SharedPreferences sharedPreferences = (SharedPreferences) requireActivity()
                .getSharedPreferences("save_creds", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rememberMe", "yes");
        editor.apply();
    }

    private boolean detailsSaved() {

        SharedPreferences sharedPreferences = (SharedPreferences) requireActivity()
                .getSharedPreferences("save_creds", Context.MODE_PRIVATE);

        String saved = sharedPreferences.getString("rememberMe", "defaultValue");

        return saved.equals("yes");
    }

    private void loadSupervisorActivity(Context context) {
        Intent supervisorHomeActivity = new Intent(getActivity(), SupervisorActivity.class);
        getActivity().finish();
        startActivity(supervisorHomeActivity);
        Toast.makeText(context, "Welcome back !", Toast.LENGTH_SHORT).show();
    }
    
    private void loadAdminActivity(Context context) {
        Intent adminIntent = new Intent(getActivity(), AdminMainActivity.class);
        getActivity().finish();
        startActivity(adminIntent);
        Toast.makeText(context, "Welcome back !", Toast.LENGTH_SHORT).show();
    }

    private void loadHomeActivity(Context context) {
        Intent homeActivity = new Intent(getActivity(), HomeActivity.class);
        getActivity().finish();
        startActivity(homeActivity);
        Toast.makeText(context, "Welcome back !", Toast.LENGTH_SHORT).show();
    }

    private Boolean validDetails(final Context context,
                                 final String userEmail,
                                 final String userPassword,
                                 final String errorMsg) {

        SharedPrefUtils utils = new SharedPrefUtils();
        MasterKey masterKey = utils.getMasterKey(context);
        SharedPreferences encryptedPreferences = utils.getEncryptedPreferences(masterKey, context);

        assert encryptedPreferences != null;
        String userDetails = encryptedPreferences.getString(userEmail + userPassword + "data", errorMsg);
        SharedPreferences.Editor editor = encryptedPreferences.edit();
        editor.putString("user_display", userDetails);
        editor.apply(); // Save shared preference


        String display = encryptedPreferences.getString("user_display", "");
        if (display.equals(errorMsg)) {
            email.setText("");
            password.setText("");
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

}
