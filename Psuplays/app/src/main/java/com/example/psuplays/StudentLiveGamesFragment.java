package com.example.psuplays;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.volley.VolleyLog.TAG;

public class StudentLiveGamesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_student_live_games, container, false);


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
                                    JSONArray scores = response.getJSONArray("score");
                                    int teams = scores.length();
                                    Log.e(TAG,scores.toString());
                                    String[] homeTeams = new String[teams];
                                    String[] awayTeams = new String[teams];
                                    String[] homeScore = new String[teams];
                                    String[] awayScore = new String[teams];

                                    for(int i = 0; i < teams; i++){
                                        JSONObject temp = scores.getJSONObject(i);
                                        homeTeams[i] = temp.getString("home_team");
                                        awayTeams[i] = temp.getString("away_team");
                                        homeScore[i] = temp.getString("home_team_score");
                                        awayScore[i] = temp.getString("away_team_score");
                                    }
                                    if(homeTeams.length != 0) {
                                        root.findViewById(R.id.tvinfo).setVisibility(View.INVISIBLE);
                                        root.findViewById(R.id.live_score_table).setVisibility(View.VISIBLE);
                                        ((TextView) root.findViewById(R.id.tvHometeam)).setText(homeTeams[0]);
                                        ((TextView) root.findViewById(R.id.tvAwayTeam)).setText(awayTeams[0]);
                                        ((TextView) root.findViewById(R.id.tvHomeScore)).setText(homeScore[0]);
                                        ((TextView) root.findViewById(R.id.tvAwayScore)).setText(awayScore[0]);
                                    }

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
        timer.scheduleAtFixedRate(t,1000,5000);

        return root;
    }
}