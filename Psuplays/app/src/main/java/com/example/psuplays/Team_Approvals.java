package com.example.psuplays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;

public class Team_Approvals extends DialogFragment {

        public team_approvalListener listener;

    public interface team_approvalListener{
        public void approveTeam(String id,String name,String description,String sport) throws JSONException;
        public void rejectTeam(String id,String name,String description,String sport) throws  JSONException;
    }

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String team_id = getArguments().getString("id");
            final String name = getArguments().getString("name");
            final String description = getArguments().getString("description");
            final String sport = getArguments().getString("sport");

            builder.setTitle("Team Approval");
            builder.setMessage("Team name: " + name + "\nDescription: " + description + "\nSport: " + sport)
                    .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                listener.approveTeam(team_id,name,description,sport);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })

                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })

                    .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                listener.rejectTeam(team_id,name,description,sport);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            return builder.create();
        }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (team_approvalListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement team_approvalListener");
        }
    }

}
