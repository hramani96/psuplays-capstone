package com.example.psuplays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.volley.VolleyLog.TAG;

public class Student_Dashboard extends AppCompatActivity implements logoutDialog.logoutDialogListener {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPref;

    public static Timer timer;
    public static TimerTask t;

    public static String[] sport_ids;
    public static String[] sport_names;
    public static String[] sport_max_teams;
    public static String[] sport_max_players;

    public String[] teams;
    public String[] sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dasboard);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.student_drawer_layout);
        NavigationView navigationView = findViewById(R.id.student_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_live_games, R.id.nav_sports, R.id.nav_users)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student__dasboard, menu);
        ((TextView)findViewById(R.id.student_user_id)).setText(sharedPref.getString("username",""));
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        logoutDialog dialog = new logoutDialog();
        dialog.show(getSupportFragmentManager(),"logout dialog");
    }

    public void logout(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("log_in_preference",false);
        editor.commit();

        timer.cancel();
        timer.purge();
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void playStream(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.PLAYBACK_URL));
        startActivity(intent);
    }



    public void getSports() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/sport/getAllSports/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");
                            JSONArray sports = response.getJSONArray("sports");
                            int no_sports = sports.length();
                            String[] id = new String[no_sports];
                            String[] name = new String[no_sports];
                            String[] team_capacity = new String[no_sports];
                            String[] player_capacity = new String[no_sports];

                            for(int i = 0; i < no_sports; i++){
                                JSONObject temp = sports.getJSONObject(i);
                                id[i] = temp.getString("id");
                                name[i] = temp.getString("name");
                                team_capacity[i] = temp.getString("max_teams_capacity");
                                player_capacity[i] = temp.getString("max_players_capacity");
                            }

                            sport_ids = id;
                            sport_names = name;
                            sport_max_teams = team_capacity;
                            sport_max_players = player_capacity;

                            Log.e(TAG,response.toString());
                            ArrayList<String> sports_list = new ArrayList<>();
                            for(int i = 0; i < no_sports; i++){
                                String temp = name[i];
                                sports_list.add(temp);
                                Log.e(TAG,temp);
                            }

                            ArrayAdapter adapter = new ArrayAdapter<String>(Student_Dashboard.this,android.R.layout.simple_list_item_1,sports_list);
                            ListView list = (ListView) findViewById(R.id.lvStudentSportsList);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(Student_Dashboard.this, Student_sport.class);
                                    intent.putExtra("sport", sport_names[i]);
                                    intent.putExtra("sport_id",sport_ids[i]);
                                    intent.putExtra("max_teams",sport_max_teams[i]);
                                    intent.putExtra("max_players",sport_max_players[i]);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Log.e(TAG,"Sports list update Successful");
                        }
                        else {
                            Toast.makeText(Student_Dashboard.this, "Sports list update failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Student_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void refreshScore(){
        t = new TimerTask() {
            @Override
            public void run() {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http:73.188.242.140:8888/score/getActiveGames/";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                String status = "";
                                try {
                                    status = response.getString("status");
                                    JSONArray games = response.getJSONArray("games");
                                    int no_games = games.length();
                                    Log.e(TAG,games.toString());
                                    String[] homeTeams = new String[no_games];
                                    String[] awayTeams = new String[no_games];
                                    Integer[] homeScore = new Integer[no_games];
                                    Integer[] awayScore = new Integer[no_games];

                                    for(int i = 0; i < no_games; i++){
                                        JSONObject temp = games.getJSONObject(i);
                                        homeTeams[i] = temp.getString("team_1");
                                        awayTeams[i] = temp.getString("team_2");
                                        homeScore[i] = temp.getInt("score_1");
                                        awayScore[i] = temp.getInt("score_2");
                                    }
                                    if(homeTeams.length != 0) {
                                        findViewById(R.id.tvinfo).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.live_score_table).setVisibility(View.VISIBLE);
                                        findViewById(R.id.vviewLiveGame).setVisibility(View.VISIBLE);
                                        findViewById(R.id.btPlayStream).setVisibility(View.VISIBLE);
                                        ((VideoView)findViewById(R.id.vviewLiveGame)).start();
                                        ((TextView) findViewById(R.id.tvHometeam)).setText(homeTeams[0]);
                                        ((TextView) findViewById(R.id.tvAwayTeam)).setText(awayTeams[0]);
                                        ((TextView) findViewById(R.id.tvHomeScore)).setText(String.valueOf(homeScore[0]));
                                        ((TextView) findViewById(R.id.tvAwayScore)).setText(String.valueOf(awayScore[0]));
                                    }
                                    else{
                                        findViewById(R.id.tvinfo).setVisibility(View.VISIBLE);
                                        findViewById(R.id.live_score_table).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.vviewLiveGame).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.btPlayStream).setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(status.equals("success")){
                                    Toast.makeText(getApplicationContext(),"Score update successful",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Score update failed!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                            }
                        });


                queue.add(jsonObjectRequest);
            }
        };
        timer.scheduleAtFixedRate(t,1000,5000);
    }

    public void loadteams()throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/sport/getSchedule/";
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

                            JSONArray team = response.getJSONArray("schedule");
                            int no_teams = team.length();
                            String[] my_teams = new String[no_teams];
                            String[] my_sports = new String[no_teams];

                            for(int i = 0; i < no_teams; i++){
                                JSONObject temp = team.getJSONObject(i);
                                my_teams[i] = temp.getString("team1");
                                my_sports[i] = temp.getString("date");
                            }

                            teams = my_teams;
                            sports = my_sports;

                            ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

                            int l = teams.length;
                            for(int i =0; i < l; i++) {
                                Map<String,Object> listItemMap = new HashMap<String,Object>();
                                listItemMap.put("team", teams[i]);
                                listItemMap.put("sport", sports[i]);
                                itemDataList.add(listItemMap);
                            }

                            SimpleAdapter simpleAdapter = new SimpleAdapter(Student_Dashboard.this,itemDataList,android.R.layout.simple_list_item_2,
                                    new String[]{"team","sport"},new int[]{android.R.id.text1,android.R.id.text2});


                            ListView list = (ListView) findViewById(R.id.lvYourTeams);
                            list.setAdapter(simpleAdapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //updateSports(i);
                                    Intent intent = new Intent(Student_Dashboard.this, TeamPage.class);
                                    intent.putExtra("member",true);
                                    intent.putExtra("team", teams[i]);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Toast.makeText(Student_Dashboard.this,"My teams loaded Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Student_Dashboard.this, "My teams loading failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Student_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);

    }

}
