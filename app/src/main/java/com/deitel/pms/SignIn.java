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

        Button btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        Button btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        Button btnResetPassword = (Button) view.findViewById(R.id.btnResetPassword);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - add functionality
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
