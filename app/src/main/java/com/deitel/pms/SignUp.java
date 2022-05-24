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

import com.deitel.pms.admin.AdminMainActivity;
import com.deitel.pms.recommender.RecommenderActivity;
import com.deitel.pms.student.HomeActivity;
import com.deitel.pms.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class SignUp extends Fragment {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user = new User();
    FirestoreUtils u = new FirestoreUtils();

    private Button btnAlreadyMember;
    private Button btnSignUp;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText uniAccessCode;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


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

        btnSignUp.setOnClickListener(view12 -> {

            final String finalEmail = email.getText().toString();
            final String finalPassword = password.getText().toString();
            final String finalConfirmPassword = confirmPassword.getText().toString();
            final String finalUniAccessCode = uniAccessCode.getText().toString();
            final String TAG = "Sign-Up";

            if (finalEmail.isEmpty() || finalPassword.isEmpty() || finalConfirmPassword.isEmpty() ||
                    finalUniAccessCode.isEmpty()) {
                Toast.makeText(getContext(), "Missing values !", Toast.LENGTH_SHORT).show();
            } else if (!finalPassword.equals(finalConfirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match !", Toast.LENGTH_SHORT).show();
                password.getText().clear();
                confirmPassword.getText().clear();
            } else if (finalPassword.length() < 6){
                Toast.makeText(getContext(), "Password length too short", Toast.LENGTH_SHORT).show();
                password.getText().clear();
                confirmPassword.getText().clear();
            } else if (validStudentEmailAndAccessCode(finalEmail, finalUniAccessCode)) {
                createAuthenticatedAccount(finalEmail, finalPassword, TAG, true, false, getContext());
                user.setUserId(requireActivity(), finalEmail);
            } else if (validSupervisorEmailAndAccessCode(finalEmail, finalUniAccessCode)) {
                createAuthenticatedAccount(finalEmail, finalPassword, TAG, false, false, getContext());
                user.setUserId(requireActivity(), finalEmail);
            } else if (validAdminEmailAndAccessCode(finalEmail, finalUniAccessCode)) {
                createAuthenticatedAccount(finalEmail, finalPassword, TAG, false, true, getContext());
                user.setUserId(requireActivity(), finalEmail+"-"+ActiveUniAdmin.EXETER_ADMIN.getValue());
            }
        });

        btnAlreadyMember.setOnClickListener(view1 -> {
            // Change button color onClick
            ButtonUtils.textButtonColorChange(btnAlreadyMember);
            replaceFragment(new SignIn());
        });
    }

    private void setAccountCreatedTrue(String supervisor_collection_path, String finalEmail,
                                       String field_supervisor_account_created) {

        Map<String, Object> accountCreated = new HashMap<>();
        accountCreated.put(field_supervisor_account_created, true);

        db.collection(supervisor_collection_path)
                .document(finalEmail)
                .set(accountCreated)
                .addOnSuccessListener(unused -> Log.d("LOGGER", "DocumentSnapshot added with ID: " + finalEmail))
                .addOnFailureListener(e -> Log.w("LOGGER", "Error adding document", e));
    }

    private boolean validAdminEmailAndAccessCode(String finalEmail, String finalUniAccessCode) {
        String[] parts = finalEmail.split("@");
        for (ActiveUniAdmin uni : ActiveUniAdmin.values()) {
            try {
                if (uni.getKey().equals(parts[1]) && uni.getValue().equals(Integer.parseInt(finalUniAccessCode))) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean validStudentEmailAndAccessCode(String email, String accessCode) {

        String[] parts = email.split("@");
        for (ActiveUnisStudents uni : ActiveUnisStudents.values()) {
            try {
                if (uni.getKey().equals(parts[1]) && uni.getValue().equals(Integer.parseInt(accessCode))) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean validSupervisorEmailAndAccessCode(String email, String accessCode) {
        String[] parts = email.split("@");
        for (ActiveUnisSupervisors uni : ActiveUnisSupervisors.values()) {
            try {
                if (uni.getKey().equals(parts[1]) && uni.getValue().equals(Integer.parseInt(accessCode))) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private void createStudentAccount(String email, String TAG) {

        // Create user info
        User newUser = new User();
        newUser.clearIdPreferences(requireActivity());
        newUser.setUserId(requireActivity(), email);

        Map<String, Object> user = new HashMap<>();
        user.put("project title", null);
        user.put("project description", null);
        user.put("supervisor name", null);
        user.put("supervisor email", null);
        user.put("approved project", false);

        db.collection("users")
                .document(email)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot added with ID: " + email))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void createAdminAccount(String email, String TAG) {
        User newUser = new User();
        newUser.clearIdPreferences(requireActivity());
        newUser.setUserId(requireActivity(), email+"-"+ActiveUniAdmin.EXETER_ADMIN.getValue());

        Map<String, Object> user = new HashMap<>();
        user.put("adminId", ActiveUniAdmin.EXETER_ADMIN.getValue());

        db.collection("admin")
                .document(email)
                .set(user)
                .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot added with ID: " + email))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
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

    private void loadStudentWorkspace(Context context) {
        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
        startActivity(homeIntent);
        getActivity().finish();
        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show();
    }

    private void loadSupervisorWorkspace(Context context) {
        Intent workspace = new Intent(getActivity(), SupervisorActivity.class);
        startActivity(workspace);
        getActivity().finish();
        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show();
    }

    private void loadAdminWorkspace(Context context) {
        Intent workspace = new Intent(getActivity(), AdminMainActivity.class);
        startActivity(workspace);
        getActivity().finish();
        Toast.makeText(context, "Account created !", Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Object object) {
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // do nothing
        }
    }

    private void createAuthenticatedAccount(String userEmail, String userPassword, String TAG,
                                            Boolean student, Boolean admin, Context context) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        if ( !student && !admin ) {
                            setAccountCreatedTrue(u.getSUPERVISOR_COLLECTION_PATH(), userEmail, u.getFIELD_SUPERVISOR_ACCOUNT_CREATED());
                            saveToPrefs(userEmail, userPassword, context);
                            loadSupervisorWorkspace(context);
                            sendWelcomeMessage("supervisors", userEmail);
                        }
                        if ( student && !admin) {
                            createStudentAccount(userEmail, TAG);
                            saveToPrefs(userEmail, userPassword, context);
                            loadStudentWorkspace(context);
                            sendWelcomeMessage("users", userEmail);
                        }
                        if ( !student && admin) {
                            createAdminAccount(userEmail, TAG);
                            loadAdminWorkspace(context);
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendWelcomeMessage(String collectionId, String userId) {
        FirestoreUtils.createNotification(collectionId,
                "Adam (Dev Team)", userId,
                "Welcome !",
                "Hello and warm welcome to the best project " +
                        "management app for final year project students and supervisors." +
                        " Make yourself at home.");
    }
}
