package com.deitel.pms.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.deitel.pms.ActiveUniAdmin;
import com.deitel.pms.MainActivity;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.supervisor_profile, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = new User();

        Button signOut = view.findViewById(R.id.btnSupervisorAccountSignOut);
        Button deleteAccount = view.findViewById(R.id.btnSupervisorAccountDeleteAccount);

        TextView adminEmail = view.findViewById(R.id.spTvEmail);
        adminEmail.setText(user.getUserId(requireActivity())
                .split("-"+ ActiveUniAdmin.EXETER_ADMIN.getValue())[0]);

        TextView studentCount = view.findViewById(R.id.spTvGroupSize);
        TextView tvStuCount = view.findViewById(R.id.tv3);
        tvStuCount.setText("Student count");

        TextView supervisorCount = view.findViewById(R.id.spTvModuleLead);
        TextView tvSupCount = view.findViewById(R.id.tv4);
        tvSupCount.setText("Supervisor count");

        setCount("users", studentCount);
        setCount("supervisors", supervisorCount);

        signOut.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().finish();
            startActivity(intent);
            Toast.makeText(getContext(), "Goodbye!", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        });

    }

    private void setCount(String collection, TextView tv) {
        FirebaseFirestore.getInstance()
                .collection(collection)
                .get()
                .addOnCompleteListener(task -> {
                    int count = 0;
                    for (int i = 0; i < task.getResult().size(); i++) {
                        count = count + 1;
                    }
                    tv.setText(String.valueOf(count));
                }).addOnFailureListener(e -> tv.setText("0"));
    }
}
