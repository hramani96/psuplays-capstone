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
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class Admin_Dashboard extends AppCompatActivity implements admin_form.admin_formListener, updateScoreDialog.updateScoreListener, logoutDialog.logoutDialogListener, create_sport_form.create_sportListener{

    private AppBarConfiguration mAppBarConfiguration;

    public static String[] admin_ids;
    public static String[] firstNames;
    public static String[] lastNames;
    public static String[] emails;
    public static String[] roles;
    public static String[] sport_ids;
    public static String[] sport_names;
    public static String[] sport_max_teams;
    public static String[] sport_max_players;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
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
        ((TextView)findViewById(R.id.user_name)).setText("Username here");
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

        getAdmins(this.getCurrentFocus());
    }

    public void updateScore(View view) {
        updateScoreDialog update = new updateScoreDialog();
        update.show(getSupportFragmentManager(), "update score");
    }

    public void sendScore(String hteam,String ateam, int hscore, int ascore) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/add/";
        JSONObject form = new JSONObject();
        form.put("home_team",hteam);
        form.put("away_team",ateam);
        form.put("home_team_score",hscore);
        form.put("away_team_score",ascore);

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
                            Toast.makeText(Admin_Dashboard.this,"Score update successful",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Admin_Dashboard.this, "Score update failed!", Toast.LENGTH_SHORT).show();
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
        getSports(this.getCurrentFocus());
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
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void getAdmins(final View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/admin/getAllAdmins";

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
                            ListView list = (ListView) view.findViewById(R.id.lvAdmins);
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
        getAdmins(this.getCurrentFocus());
    }

    public void getSports(final View view) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/sport/getAllSports";

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

                            ArrayAdapter adapter = new ArrayAdapter<String>(Admin_Dashboard.this,android.R.layout.simple_list_item_1,sports_list);
                            ListView list = (ListView) view.findViewById(R.id.lvSportsList);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //updateSports(i);
                                    String sportName = sport_names[i];
                                    Intent intent = new Intent(Admin_Dashboard.this, admin_sport.class);
                                    intent.putExtra("sport", sportName);
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
}
