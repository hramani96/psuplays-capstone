package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class TeamPage extends AppCompatActivity {

    public static String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_page);
        String team = getIntent().getExtras().getString("team");
        Boolean member = getIntent().getExtras().getBoolean("member");
        setTitle(team);
        ((TextView)findViewById(R.id.tvRoaster)).setText(team + "'s Roaster:");
        if(member){
            findViewById(R.id.tvAddPlayer).setVisibility(View.VISIBLE);
            findViewById(R.id.btAddPlayer).setVisibility(View.VISIBLE);
            findViewById(R.id.etPlayerEmail).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.btAddPlayer)).setClickable(true);
            ((EditText)findViewById(R.id.etPlayerEmail)).setEnabled(true);
        }
        try {
            loadRoaster();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadRoaster() throws JSONException{

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/player/getPlayersForTeam/";
        JSONObject form = new JSONObject();
        form.put("id",getIntent().getExtras().getInt("id"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            Log.e(TAG,response.toString());
                            status = response.getString("status");
                            JSONArray players = response.getJSONArray("teams");
                            int no_players = players.length();
                            String[] name = new String[no_players];

                            for(int i = 0; i < no_players; i++){
                                JSONObject temp = players.getJSONObject(i);
                                name[i] = temp.getString("user__first_name") + " " + temp.getString("user__last_name");
                            }

                            names = name;

                            Log.e(TAG,response.toString());
                            ArrayList<String> teams_list = new ArrayList<>();
                            for(int i = 0; i < no_players; i++){
                                String temp = name[i];
                                teams_list.add(temp);
                            }

                            ArrayAdapter adapter = new ArrayAdapter<String>(TeamPage.this,android.R.layout.simple_list_item_1,teams_list);
                            ListView list = (ListView) findViewById(R.id.lvRoaster);
                            list.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){

                            Toast.makeText(TeamPage.this,"Roaster loaded Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(TeamPage.this, "Roaster loading failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TeamPage.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);


    }

    public void addPlayer(View view) throws JSONException{

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/player/create/";
        JSONObject form = new JSONObject();
        form.put("team_id",getIntent().getExtras().getInt("id"));
        form.put("email",((EditText) findViewById(R.id.etPlayerEmail)).getText().toString());

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
                            try {
                                loadRoaster();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(TeamPage.this,"Player added successfully",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(TeamPage.this, "Player creation failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TeamPage.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }
}
