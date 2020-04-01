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

public class updateScoreDialog extends DialogFragment {

    private updateScoreListener listener;

    public interface updateScoreListener{
        public void sendScore(String hometeam,String awayteam, int hscore, int ascore) throws JSONException;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_update_score_dialog, null);

        builder.setView(view)
                .setTitle("Update Score")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                        dismiss();
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: connect to the database to add an admin
                        String hometeam = (((TextView) view.findViewById(R.id.etHometeam)).getText()).toString();
                        String awayteam = (((TextView) view.findViewById(R.id.etawayscore)).getText()).toString();
                        int hscore = Integer.valueOf((((TextView) view.findViewById(R.id.etHomescore)).getText()).toString());
                        int ascore = Integer.valueOf((((TextView) view.findViewById(R.id.etawayscore)).getText()).toString());

                        try {
                            listener.sendScore(hometeam,awayteam,hscore,ascore);
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
            listener = (updateScoreDialog.updateScoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement updateScoreListener");
        }
    }
}
