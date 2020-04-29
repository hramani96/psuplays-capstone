package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Admin_Dashboard extends AppCompatActivity implements admin_form.admin_formListener, logoutDialog.logoutDialogListener, create_sport_form.create_sportListener, Team_Approvals.team_approvalListener{

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPref;

    public static String[] admin_ids;
    public static String[] firstNames;
    public static String[] lastNames;
    public static String[] emails;
    public static String[] roles;
    public static Integer[] sport_ids;
    public static String[] sport_names;
    public static Integer[] sport_max_teams;
    public static Integer[] sport_max_players;
    public static String[] approved_team_names;
    public static String[] approved_team_sports;
    public static String[] pending_team_ids;
    public static String[] pending_team_names;
    public static String[] pending_team_description;
    public static String[] pending_team_sports;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.admin_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_live_games, R.id.nav_admin_sports, R.id.nav_manage_admin,R.id.nav_team_approval)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_dashboard, menu);
        ((TextView)findViewById(R.id.admin_user_id)).setText(sharedPref.getString("username",""));
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void adminForm(View view) {
        admin_form adminForm = new admin_form();
        adminForm.show(getSupportFragmentManager(), "Create Admin");
    }

    public void createSport(View view) {
        create_sport_form createSport = new create_sport_form();
        createSport.show(getSupportFragmentManager(), "Add Sport");
    }

    public void addAdmin(String firstName,String lastName, String email, String password) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/user/create/";
        JSONObject form = new JSONObject();
        form.put("first_name",firstName);
        form.put("last_name",lastName);
        form.put("email",email);
        form.put("password",password);
        form.put("password_conf",password);
        form.put("role","Admin");

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
                            Toast.makeText(Admin_Dashboard.this,"Admin created successful",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Admin creation failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);

        getAdmins();
    }

    public void updateScore(View view) {
        Intent intent = new Intent(Admin_Dashboard.this,UpdateLiveScore.class);
        intent.putExtra("home_team_name",(((TextView)findViewById(R.id.etHomeTeam)).getText()).toString());
        intent.putExtra("away_team_name",(((TextView)findViewById(R.id.etAwayTeam)).getText()).toString());
        intent.putExtra("home_team_score",(((TextView)findViewById(R.id.etHomeTeamScore)).getText()).toString());
        intent.putExtra("away_team_score",(((TextView)findViewById(R.id.etAwayTeamScore)).getText()).toString());
        startActivity(intent);
        finish();
    }

    public void createSport(String name, String teams, String players) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/sport/create/";
        JSONObject form = new JSONObject();
        form.put("name",name);
        form.put("max_teams_capacity",teams);
        form.put("max_players_capacity",players);

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
                            Toast.makeText(Admin_Dashboard.this,"Sport created successfully",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Sport creation failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
        getSports();
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

        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void getAdmins() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/admin/getAllAdmins/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                            JSONArray admins = response.getJSONArray("admins");
                            int no_admins = admins.length();
                            Log.e(TAG,admins.toString());
                            String[] id = new String[no_admins];
                            String[] firstname = new String[no_admins];
                            String[] lastname = new String[no_admins];
                            String[] email = new String[no_admins];
                            String[] role = new String[no_admins];

                            for(int i = 0; i < no_admins; i++){
                                JSONObject temp = admins.getJSONObject(i);
                                id[i] = temp.getString("id");
                                firstname[i] = temp.getString("first_name");
                                lastname[i] = temp.getString("last_name");
                                email[i] = temp.getString("email");
                                role[i] = temp.getString("role");
                            }

                            admin_ids = id;
                            firstNames = firstname;
                            lastNames = lastname;
                            emails = email;
                            roles = role;

                            Log.e(TAG,response.toString());
                            ArrayList<String> admins_list = new ArrayList<>();
                            for(int i = 0; i < no_admins; i++){
                                String temp = firstname[i]+ " " + lastname[i];
                                admins_list.add(temp);
                                Log.e(TAG,temp);
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(Admin_Dashboard.this,android.R.layout.simple_list_item_1,admins_list);
                            ListView list = (ListView) findViewById(R.id.lvAdmins);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    updateAdmin(i);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Log.e(TAG,"Admin list update Successful");
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Admin list update failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    public void updateAdmin(int i){
        Toast.makeText(this,firstNames[i] + " " + lastNames[i] + " Item was selected", Toast.LENGTH_SHORT).show();
        getAdmins();
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
                            Integer[] id = new Integer[no_sports];
                            String[] name = new String[no_sports];
                            Integer[] team_capacity = new Integer[no_sports];
                            Integer[] player_capacity = new Integer[no_sports];

                            for(int i = 0; i < no_sports; i++){
                                JSONObject temp = sports.getJSONObject(i);
                                id[i] = temp.getInt("id");
                                name[i] = temp.getString("name");
                                team_capacity[i] = temp.getInt("max_teams_capacity");
                                player_capacity[i] = temp.getInt("max_players_capacity");
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

                            ArrayAdapter adapter = new ArrayAdapter<String>(Admin_Dashboard.this,android.R.layout.simple_list_item_1,sports_list);
                            ListView list = (ListView) findViewById(R.id.lvSportsList);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //updateSports(i);
                                    Intent intent = new Intent(Admin_Dashboard.this, admin_sport.class);
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
                            Toast.makeText(Admin_Dashboard.this, "Sports list update failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    public void getApprovedTeams() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/team/getApprovedTeams/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");
                            JSONArray approved_teams = response.getJSONArray("teams");
                            int no_approved_teams = approved_teams.length();
                            String[] approved_tname = new String[no_approved_teams];
                            String[] approved_tsport = new String[no_approved_teams];

                            for(int i = 0; i < no_approved_teams; i++){
                                JSONObject temp = approved_teams.getJSONObject(i);
                                approved_tname[i] = temp.getString("name");
                                approved_tsport[i] = temp.getString("sport__name");
                            }

                            approved_team_names = approved_tname;
                            approved_team_sports = approved_tsport;

                            ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

                            int l = approved_team_names.length;
                            for(int i =0; i < l; i++) {
                                Map<String,Object> listItemMap = new HashMap<String,Object>();
                                listItemMap.put("name", approved_team_names[i]);
                                listItemMap.put("sport", approved_team_sports[i]);
                                itemDataList.add(listItemMap);
                            }

                            SimpleAdapter simpleAdapter = new SimpleAdapter(Admin_Dashboard.this,itemDataList,android.R.layout.simple_list_item_2,
                                    new String[]{"name","sport"},new int[]{android.R.id.text1,android.R.id.text2});


                            ListView list = (ListView) findViewById(R.id.lvApprovedTeams);
                            list.setAdapter(simpleAdapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Log.e(TAG,"Approved teams list update Successful");
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Approved teams list update failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    public void getPendingApprovals() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/team/getNewTeams/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");
                            JSONArray pending_teams = response.getJSONArray("teams");
                            int no_pending_teams = pending_teams.length();
                            String[] pending_tid = new String[no_pending_teams];
                            final String[] pending_tname = new String[no_pending_teams];
                            String[] pending_tdesc = new String[no_pending_teams];
                            String[] pending_tsport = new String[no_pending_teams];

                            for(int i = 0; i < no_pending_teams; i++){
                                JSONObject temp = pending_teams.getJSONObject(i);
                                pending_tid[i] = temp.getString("id");
                                pending_tname[i] = temp.getString("name");
                                pending_tdesc[i] = temp.getString("description");
                                pending_tsport[i] = temp.getString("sport__name");
                            }

                            pending_team_ids = pending_tid;
                            pending_team_names = pending_tname;
                            pending_team_description = pending_tdesc;
                            pending_team_sports = pending_tsport;

                            ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

                            int l = pending_team_names.length;
                            for(int i =0; i < l; i++) {
                                Map<String,Object> listItemMap = new HashMap<String,Object>();
                                listItemMap.put("name", pending_team_names[i]);
                                listItemMap.put("sport", pending_team_sports[i]);
                                itemDataList.add(listItemMap);
                            }

                            SimpleAdapter simpleAdapter = new SimpleAdapter(Admin_Dashboard.this,itemDataList,android.R.layout.simple_list_item_2,
                                    new String[]{"name","sport"},new int[]{android.R.id.text1,android.R.id.text2});

                            ListView list = (ListView) findViewById(R.id.lvPendingTeams);
                            list.setAdapter(simpleAdapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Bundle args = new Bundle();
                                    args.putString("id", pending_team_ids[i]);
                                    args.putString("name", pending_team_names[i]);
                                    args.putString("description", pending_team_description[i]);
                                    args.putString("sport", pending_team_sports[i]);

                                    Team_Approvals team_approve = new Team_Approvals();
                                    team_approve.setArguments(args);
                                    team_approve.show(getSupportFragmentManager(),"team approval dialog");
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Log.e(TAG,"Pending teams list update Successful");
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Pending teams list update failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    @Override
    public void approveTeam(String id, String name, String description, String sport) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/team/ApproveTeam/";
        JSONObject form = new JSONObject();
        form.put("id",id);
        form.put("name",name);
        form.put("description",description);
        form.put("sport__name",sport);

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
                            getApprovedTeams();
                            getPendingApprovals();
                            Toast.makeText(Admin_Dashboard.this,"Team Approved Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            getApprovedTeams();
                            getPendingApprovals();
                            Toast.makeText(Admin_Dashboard.this, "Team Approval failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    @Override
    public void rejectTeam(String id, String name, String description, String sport) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/team/DenyTeam/";
        JSONObject form = new JSONObject();
        form.put("id",id);
        form.put("name",name);
        form.put("description",description);
        form.put("sport__name",sport);

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
                            getApprovedTeams();
                            getPendingApprovals();
                            Toast.makeText(Admin_Dashboard.this,"Team Denial Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            getApprovedTeams();
                            getPendingApprovals();
                            Toast.makeText(Admin_Dashboard.this, "Team Denial failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);

    }

    public void startStream(View view){
        Intent intent = new Intent(Admin_Dashboard.this,livevideouploader.class);
        intent.putExtra("home_team_name",(((TextView)findViewById(R.id.etHomeTeam)).getText()).toString());
        intent.putExtra("away_team_name",(((TextView)findViewById(R.id.etAwayTeam)).getText()).toString());
        intent.putExtra("home_team_score",(((TextView)findViewById(R.id.etHomeTeamScore)).getText()).toString());
        intent.putExtra("away_team_score",(((TextView)findViewById(R.id.etAwayTeamScore)).getText()).toString());
        startActivity(intent);
        finish();
    }

    public void cancelGame(View view) {
        EditText home = (EditText) findViewById(R.id.etHomeTeam);
        EditText hscore = (EditText) findViewById(R.id.etHomeTeamScore);
        EditText away = (EditText) findViewById(R.id.etAwayTeam);
        EditText ascore = (EditText) findViewById(R.id.etAwayTeamScore);
        String t1 = home.getText().toString();
        String t2 = away.getText().toString();
        int t1score = Integer.valueOf(hscore.getText().toString());
        int t2score = Integer.valueOf(ascore.getText().toString());
        try {
            endGame(t1,t1score,t2,t2score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createGame(View view) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/createGame/";
        JSONObject form = new JSONObject();
        final EditText home = (EditText) findViewById(R.id.etHomeTeam);
        final EditText hscore = (EditText) findViewById(R.id.etHomeTeamScore);
        final EditText away = (EditText) findViewById(R.id.etAwayTeam);
        final EditText ascore = (EditText) findViewById(R.id.etAwayTeamScore);

        form.put("team_1",home.getText().toString());
        form.put("score_1",hscore.getText().toString());
        form.put("team_2",away.getText().toString());
        form.put("score_2",ascore.getText().toString());
        form.put("active","Y");

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
                            Toast.makeText(Admin_Dashboard.this,"Game Created Successful!",Toast.LENGTH_SHORT).show();
                            home.setEnabled(false);
                            hscore.setEnabled(false);
                            away.setEnabled(false);
                            ascore.setEnabled(false);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("home_team",home.getText().toString());
                            editor.putString("away_team",away.getText().toString());
                            editor.commit();
                            verifyGame();
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Game Creation failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void verifyGame() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/getActiveGames/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");
                            JSONArray active_games = response.getJSONArray("games");
                            JSONObject temp = active_games.getJSONObject(0);
                            String home = temp.getString("team_1");
                            String away = temp.getString("team_2");
                            Integer id = temp.getInt("id");
                            if(sharedPref.getString("home_team","").equals(home)&&sharedPref.getString("away_team","").equals(away)){
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("game_id",id);
                                editor.commit();
                                (findViewById(R.id.btCreateGame)).setVisibility(View.GONE);
                                (findViewById(R.id.btLiveStream)).setVisibility(View.VISIBLE);
                                (findViewById(R.id.tvOR)).setVisibility(View.VISIBLE);
                                (findViewById(R.id.btupdateScore)).setVisibility(View.VISIBLE);
                                (findViewById(R.id.btCancelGame)).setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Log.e(TAG,"Game id retrieved Successful");
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Game id retrieve failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    public void endGame(String t1, int t1score, String t2, int t2score) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/endGame/";
        JSONObject form = new JSONObject();
        final EditText home = (EditText) findViewById(R.id.etHomeTeam);
        final EditText hscore = (EditText) findViewById(R.id.etHomeTeamScore);
        final EditText away = (EditText) findViewById(R.id.etAwayTeam);
        final EditText ascore = (EditText) findViewById(R.id.etAwayTeamScore);

        form.put("id",sharedPref.getInt("game_id",0));
        form.put("team_1",t1);
        form.put("score_1",t1score);
        form.put("team_2",t2);
        form.put("score_2",t2score);

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
                            Toast.makeText(Admin_Dashboard.this,"Game ended/canceled Successful!",Toast.LENGTH_SHORT).show();
                            home.setEnabled(true);
                            hscore.setEnabled(true);
                            away.setEnabled(true);
                            ascore.setEnabled(true);
                            (findViewById(R.id.btCreateGame)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.btLiveStream)).setVisibility(View.GONE);
                            (findViewById(R.id.tvOR)).setVisibility(View.GONE);
                            (findViewById(R.id.btupdateScore)).setVisibility(View.GONE);
                            (findViewById(R.id.btCancelGame)).setVisibility(View.GONE);
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Game ending/cancellation failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Admin_Dashboard.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
