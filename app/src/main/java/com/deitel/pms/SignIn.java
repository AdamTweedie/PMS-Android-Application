package com.deitel.pms;

import com.deitel.pms.student.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class SignIn extends Fragment {

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

        // TODO - sort if user account is student or supervisor and add the relevant activity following

        Context context = getContext();
        if (detailsSaved()) {
            loadHomeActivity(context);
        }

        User user = new User();
        Button btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        Button btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        btnResetPassword = (Button) view.findViewById(R.id.btnResetPassword);
        CheckBox rememberCredentials = (CheckBox) view.findViewById(R.id.cbRememberLoginCredentials);
        email = (EditText) view.findViewById(R.id.etUserEmail);
        password = (EditText) view.findViewById(R.id.etUserPassword);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userEmail = email.getText().toString();
                final String userPassword = password.getText().toString();
                final String errorMsg = "Username or Password is Incorrect";

                if (!validDetails(context, userEmail, userPassword, errorMsg)) {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();

                } else if (validDetails(context, userEmail, userPassword, errorMsg)) { // TODO - && account created = true
                    user.clearIdPreferences(requireActivity());
                    user.setUserId(requireActivity(), userEmail);
                    if (user.getUserId(requireActivity()).contains("supervisor")) { // and is a supervisor account
                        if (rememberCredentials.isChecked()) {
                            saveUserCredentials();
                        }
                        loadSupervisorActivity(context);
                        System.out.println("Logged in with ID - " + user.getUserId(requireActivity()));
                    } else {
                        user.clearIdPreferences(requireActivity());
                        user.setUserId(requireActivity(), userEmail);
                        if (rememberCredentials.isChecked()) {
                            saveUserCredentials();
                        }
                        System.out.println("Logged in with ID - " + user.getUserId(requireActivity()));
                        loadHomeActivity(context);
                    }
                }
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

    private void saveUserCredentials() {
        SharedPreferences sharedPreferences = (SharedPreferences) requireActivity()
                .getSharedPreferences("save_creds", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rememberMe", "yes");
        editor.apply();
    }

    private boolean detailsSaved() {
        // TODO - should probably do this through encrypted credentials
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
