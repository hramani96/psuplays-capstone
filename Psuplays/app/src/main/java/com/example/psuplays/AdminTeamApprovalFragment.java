package com.example.psuplays;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminTeamApprovalFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Admin_Dashboard)getActivity()).getApprovedTeams();
        ((Admin_Dashboard)getActivity()).getPendingApprovals();
        return inflater.inflate(R.layout.fragment_admin_team_approval, container, false);
    }

}
