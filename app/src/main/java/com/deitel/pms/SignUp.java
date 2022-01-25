package com.deitel.pms;

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

public class SignUp extends Fragment {

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
        Button btnAlreadyMember = (Button) view.findViewById(R.id.btnBackToSignIn);
        Button btnSignUp = (Button) view.findViewById(R.id.btnCreateAccount);
        EditText email = (EditText) view.findViewById(R.id.etNewEmail);
        EditText password = (EditText) view.findViewById(R.id.etNewPassword);
        EditText confirmPassword = (EditText) view.findViewById(R.id.etNewConfirmPassword);
        EditText uniAccessCode = (EditText) view.findViewById(R.id.etUniversityAccessCode);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - connect to database
                // check no fields left blank
                // encryption
                // check already not user
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


    public void replaceFragment(Object object) {
        final FragmentTransaction fragmentTransaction;
        final FragmentManager fragmentManager = getParentFragmentManager();

        // TODO - OnClick fragment changes to sign up fragment.
        final Fragment fragment = getParentFragmentManager()
                .findFragmentById(R.id.placeholder_main);

        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            //fragmentTransaction.commit();
            fragmentTransaction.add(R.id.placeholder_main, (Fragment) object);
            fragmentTransaction.commit();
        }
    }
}
