package com.example.psuplays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;


public class StudentUsersFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_student_users, container, false);
        try {
            ((Student_Dashboard)getActivity()).loadteams();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }

}