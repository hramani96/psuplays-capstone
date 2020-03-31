package com.example.psuplays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;

import java.util.Objects;

public class admin_form extends DialogFragment {

    private admin_formListener listener;

    public interface admin_formListener{
        public void addAdmin(String firstName,String lastName, String email, String password) throws JSONException;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_admin_form_dialog, null);

        builder.setView(view)
                .setTitle("Create Admin")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                        dismiss();
                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            // TODO: connect to the database to add an admin
                        String firstName = (((TextView) view.findViewById(R.id.etFirstName)).getText()).toString();
                        String lastName = (((TextView) view.findViewById(R.id.etLastName)).getText()).toString();
                        String email = (((TextView) view.findViewById(R.id.etAdminEmail)).getText()).toString();
                        String password = (((TextView) view.findViewById(R.id.etAdminPassword)).getText()).toString();

                        try {
                            listener.addAdmin(firstName,lastName,email,password);
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
            listener = (admin_formListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement admin_formListener");
        }
    }

}

