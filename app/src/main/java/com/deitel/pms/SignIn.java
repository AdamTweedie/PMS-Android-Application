package com.deitel.pms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SignIn extends Fragment {

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

        Button signUp = (Button) view.findViewById(R.id.btnSignUp);
        Button resetPassword = (Button) view.findViewById(R.id.btnResetPassword);


        // On click, push SignUp onto fragment back-stack and pop SignIn fragment off back stack
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Success!!");

                final FragmentTransaction fragmentTransaction;
                final FragmentManager fragmentManager = getParentFragmentManager();

                // TODO - OnClick fragment changes to sign up fragment.
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

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change button color onclick
                ButtonFunctions.textButtonColorChange(resetPassword);
            }
        });
    }
}
