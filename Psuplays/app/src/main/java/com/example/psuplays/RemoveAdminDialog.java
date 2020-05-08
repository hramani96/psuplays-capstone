package com.example.psuplays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import org.json.JSONException;

public class RemoveAdminDialog extends AppCompatDialogFragment {

    private removeAdminListener listener;

    public interface removeAdminListener{
        public void removeAdmin(String email) throws JSONException;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Remove Admin")
                .setMessage("Do you want to remove " + getArguments().getString("name") + " ?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: connect to the database to add a sport
                        try {
                            listener.removeAdmin(getArguments().getString("email"));
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
            listener = (removeAdminListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement removeAdminListener");
        }
    }
}

