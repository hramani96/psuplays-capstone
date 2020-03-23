package com.example.psuplays;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class StudentSportsFragment extends Fragment {

    public StudentSportsFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
       ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_student_sports, container, false);
        Button btnAddTeam = (Button) root.findViewById(R.id.createNewTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTeam CreateNewTeam = new createNewTeam();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.nav_host_fragment, CreateNewTeam, CreateNewTeam.getTag())
                        .commit();
            }
        });

        Button btnJoinExisting = (Button) root.findViewById(R.id.joinExistingTeam);
        btnJoinExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinExistingTeam JoinExistingTeam = new joinExistingTeam();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.nav_host_fragment, JoinExistingTeam, JoinExistingTeam.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });


        return root;
    }

}