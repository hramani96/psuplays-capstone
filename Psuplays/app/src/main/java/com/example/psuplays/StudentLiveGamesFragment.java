package com.example.psuplays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.psuplays.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class StudentLiveGamesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_student_live_games, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
    /*
        textView.setText("Live sports will be implemented here");
        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = "http:73.188.242.140:8888/score/show/";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                String status = "";
                                try {
                                    status = response.getString("status");
                                    ((TextView)root.findViewById(R.id.textView3)).setText(response.getString("home_team"));
                                    ((TextView)root.findViewById(R.id.textView4)).setText(response.getString("away_team"));
                                    ((TextView)root.findViewById(R.id.textView5)).setText(response.getString("home_team_score"));
                                    ((TextView)root.findViewById(R.id.textView6)).setText(response.getString("away_team_score"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(status.equals("success")){
                                    Toast.makeText(getActivity(),"Score update successful",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getActivity(), "Score update failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                String responseBody = null;
                                try {
                                    responseBody = new String(error.networkResponse.data, "utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                JSONObject data = null;
                                try {
                                    data = new JSONObject(responseBody);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String message = data.optString("reason");
                                Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
                            }
                        });


                queue.add(jsonObjectRequest);
            }
        };
        timer.scheduleAtFixedRate(t,1000,1000);


     */

        return root;
    }
}