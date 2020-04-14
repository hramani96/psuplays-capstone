package com.example.psuplays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class Team_Approvals extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Team Approval");
            builder.setMessage("Requesting Team:")
                    .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // TODO: If team is approved
                        }
                    })

                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })

                    .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // TODO: If team is rejected
                        }
                    });
            return builder.create();
        }

}
