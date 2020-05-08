package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class admin_sport extends AppCompatActivity {

    public static String[] team_names;
    public static Integer[] team_ids;
    public static String[] matches;
    public static String[] times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sport);
        String sport = getIntent().getExtras().getString("sport");
        Integer id = getIntent().getExtras().getInt("sport_id");
        setTitle(sport);
        TextView tv = (TextView)findViewById(R.id.tvTeams);
        tv.setText("Teams for " + sport + ":");
        TextView tv1 = (TextView)findViewById(R.id.tvSchedule);
        tv1.setText("Schedule for " + sport + ":");
        try {
            getTeams();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            getSchedule();
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
                            Integer[] id = new Integer[no_teams];

                            for(int i = 0; i < no_teams; i++){
                                JSONObject temp = teams.getJSONObject(i);
                                name[i] = temp.getString("name");
                                id[i] = temp.getInt("id");
                            }

                            team_names = name;
                            team_ids = id;

                            Log.e(TAG,response.toString());
                            ArrayList<String> teams_list = new ArrayList<>();
                            for(int i = 0; i < no_teams; i++){
                                String temp = name[i];
                                teams_list.add(temp);
                            }

                            ArrayAdapter adapter = new ArrayAdapter<String>(admin_sport.this,android.R.layout.simple_list_item_1,teams_list);
                            ListView list = (ListView) findViewById(R.id.lvSportsTeam);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //updateSports(i);
                                    Intent intent = new Intent(admin_sport.this, TeamPage.class);
                                    intent.putExtra("member",false);
                                    intent.putExtra("team", team_names[i]);
                                    intent.putExtra("id", team_ids[i]);
                                    startActivity(intent);
                                }
                            });
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

    public void getSchedule() throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/sport/getSchedule/";
        JSONObject form = new JSONObject();
        form.put("sport",getIntent().getExtras().getString("sport"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");

                            JSONArray schedule = response.getJSONArray("schedule");
                            int no_matches = schedule.length();
                            if(no_matches > 0){
                                findViewById(R.id.btGenerateSchedule).setVisibility(View.GONE);
                                findViewById(R.id.tvSchedule).setVisibility(View.VISIBLE);
                                findViewById(R.id.lvschedule).setVisibility(View.VISIBLE);

                                String[] schedule_matches = new String[no_matches];
                                String[] schedule_times = new String[no_matches];

                                for(int i = 0; i < no_matches; i++){
                                    JSONObject temp = schedule.getJSONObject(i);
                                    JSONArray team1 = temp.getJSONArray("team1");
                                    JSONArray team2 = temp.getJSONArray("team2");
                                    JSONArray date = temp.getJSONArray("date");

                                    schedule_matches[i] = team1.get(0).toString() + " vs " + team2.get(0).toString();
                                    schedule_times[i] = date.get(0).toString() + "\t" + temp.getString("time");
                                }

                                matches = schedule_matches;
                                times = schedule_times;

                                ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

                                int l = matches.length;
                                for(int i =0; i < l; i++) {
                                    Map<String,Object> listItemMap = new HashMap<String,Object>();
                                    listItemMap.put("match", matches[i]);
                                    listItemMap.put("time", times[i]);
                                    itemDataList.add(listItemMap);
                                }

                                SimpleAdapter simpleAdapter = new SimpleAdapter(admin_sport.this,itemDataList,android.R.layout.simple_list_item_2,
                                        new String[]{"match","time"},new int[]{android.R.id.text1,android.R.id.text2});


                                ListView list = (ListView) findViewById(R.id.lvschedule);
                                list.setAdapter(simpleAdapter);
                            }
                            else{
                                findViewById(R.id.btGenerateSchedule).setVisibility(View.VISIBLE);
                                findViewById(R.id.tvSchedule).setVisibility(View.INVISIBLE);
                                findViewById(R.id.lvschedule).setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Toast.makeText(admin_sport.this,"Schedule loaded Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(admin_sport.this, "Schedule loading failed!", Toast.LENGTH_SHORT).show();
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

    public void generateSchedule(View view) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/schedule/create/";
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            try {
                                getSchedule();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(admin_sport.this,"Schedule generated Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(admin_sport.this, "Schedule creation failed!", Toast.LENGTH_SHORT).show();
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
