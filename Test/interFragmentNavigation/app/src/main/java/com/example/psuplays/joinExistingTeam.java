package com.example.psuplays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class joinExistingTeam extends Fragment {

    public joinExistingTeam() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_join_existing_team, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        return root;
    }

}