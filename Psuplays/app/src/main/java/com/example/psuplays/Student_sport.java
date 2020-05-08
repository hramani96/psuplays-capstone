package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class Student_sport extends AppCompatActivity {

    SharedPreferences sharedPref;
    public static String[] team_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sport);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sport = getIntent().getStringExtra("sport");

        setTitle(sport);
        ((TextView)findViewById(R.id.tvStSportTeams)).setText("Teams for " + sport + " :");
        ((TextView)findViewById(R.id.tvSportNameCreateTeam)).setText(sport);
        try {
            loadteamlist();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestTeam(View view)throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/team/create/";
        JSONObject form = new JSONObject();
        JSONObject sport = new JSONObject();
        sport.put("id",getIntent().getExtras().getInt("sport_id"));
        sport.put("name",getIntent().getExtras().getString("sport"));
        sport.put("max_teams_capacity",getIntent().getExtras().getInt("max_teams"));
        sport.put("max_players_capacity",getIntent().getExtras().getInt("max_players"));
        form.put("sport",sport);
        form.put("name",((EditText)findViewById(R.id.etTeamName)).getText().toString());
        form.put("description",((EditText)findViewById(R.id.etTeamDescription)).getText().toString());
        form.put("creator",sharedPref.getString("username",""));
        form.put("accepted","?");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            ((EditText)findViewById(R.id.etTeamName)).setText("");
                            ((EditText)findViewById(R.id.etTeamDescription)).setText("");
                            Toast.makeText(Student_sport.this,"Team created successfully",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Student_sport.this, "Team creation failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Student_sport.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    public void loadteamlist() throws JSONException{
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

                            ArrayAdapter adapter = new ArrayAdapter<String>(Student_sport.this,android.R.layout.simple_list_item_1,teams_list);
                            ListView list = (ListView) findViewById(R.id.lvStudentSportTeams);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //updateSports(i);
                                    Intent intent = new Intent(Student_sport.this, TeamPage.class);
                                    intent.putExtra("member",false);
                                    intent.putExtra("team", team_names[i]);
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){

                            Toast.makeText(Student_sport.this,"Team list loaded Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Student_sport.this, "Team list loading failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Student_sport.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
