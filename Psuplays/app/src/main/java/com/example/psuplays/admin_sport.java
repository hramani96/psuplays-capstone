package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class admin_sport extends AppCompatActivity {

    public static String[] team_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sport);
        String sport = getIntent().getExtras().getString("sport");
        Integer id = getIntent().getExtras().getInt("sport_id");
        setTitle(sport);
        TextView tv = (TextView)findViewById(R.id.tvTeams);
        tv.setText("Teams for " + sport + ":");
        try {
            getTeams();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTeams() throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/sport/getTeamsForSport/";
        JSONObject form = new JSONObject();
        JSONObject sport = new JSONObject();
        sport.put("id",getIntent().getExtras().getInt("sport_id"));
        sport.put("name",getIntent().getExtras().getString("sport"));
        sport.put("max_teams_capacity",getIntent().getExtras().getInt("max_teams"));
        sport.put("max_players_capacity",getIntent().getExtras().getInt("max_players"));
        form.put("sport",sport);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");
                            JSONArray teams = response.getJSONArray("teams");
                            int no_teams = teams.length();
                            String[] name = new String[no_teams];

                            for(int i = 0; i < no_teams; i++){
                                JSONObject temp = teams.getJSONObject(i);
                                name[i] = temp.getString("name");
                            }

                            team_names = name;

                            Log.e(TAG,response.toString());
                            ArrayList<String> teams_list = new ArrayList<>();
                            for(int i = 0; i < no_teams; i++){
                                String temp = name[i];
                                teams_list.add(temp);
                            }

                            ArrayAdapter adapter = new ArrayAdapter<String>(admin_sport.this,android.R.layout.simple_list_item_1,teams_list);
                            ListView list = (ListView) findViewById(R.id.lvSportsTeam);
                            list.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){

                            Toast.makeText(admin_sport.this,"Team list loaded Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(admin_sport.this, "Team list loading failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(admin_sport.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
