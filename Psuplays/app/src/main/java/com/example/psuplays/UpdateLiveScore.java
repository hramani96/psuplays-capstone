package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UpdateLiveScore extends AppCompatActivity {

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_live_score);
        ((TextView)findViewById(R.id.tvLSHteam)).setText(getIntent().getExtras().getString("home_team_name"));
        ((TextView)findViewById(R.id.tvLSHScore)).setText(getIntent().getExtras().getString("home_team_score"));
        ((TextView)findViewById(R.id.tvLSATeam)).setText(getIntent().getExtras().getString("away_team_name"));
        ((TextView)findViewById(R.id.tvLSAScore)).setText(getIntent().getExtras().getString("away_team_score"));
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void updateScore(View view) throws JSONException{
        if(view.getId() == R.id.ibHAdd){
            TextView tv = (TextView)findViewById(R.id.tvLSHScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) + 1));
        }
        if(view.getId() == R.id.ibHSub){
            TextView tv = (TextView)findViewById(R.id.tvLSHScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) - 1));
        }
        if(view.getId() == R.id.ibAAdd){
            TextView tv = (TextView)findViewById(R.id.tvLSAScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) + 1));
        }
        if(view.getId() == R.id.ibASub){
            TextView tv = (TextView)findViewById(R.id.tvLSAScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) - 1));
        }

        String hteam = ((TextView)findViewById(R.id.tvLSHteam)).getText().toString();
        String ateam = ((TextView)findViewById(R.id.tvLSATeam)).getText().toString();
        int hscore = Integer.valueOf(((TextView)findViewById(R.id.tvLSHScore)).getText().toString());
        int ascore = Integer.valueOf(((TextView)findViewById(R.id.tvLSAScore)).getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/update/";
        JSONObject form = new JSONObject();
        form.put("id",sharedPref.getInt("game_id",0));
        form.put("team_1",hteam);
        form.put("score_1",hscore);
        form.put("team_2",ateam);
        form.put("score_2",ascore);

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
                            Toast.makeText(UpdateLiveScore.this,"Score updated Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(UpdateLiveScore.this, "Score update failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateLiveScore.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void endGame(View view) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/endGame/";
        JSONObject form = new JSONObject();
        String hteam = ((TextView)findViewById(R.id.tvLSHteam)).getText().toString();
        String ateam = ((TextView)findViewById(R.id.tvLSATeam)).getText().toString();
        int hscore = Integer.valueOf(((TextView)findViewById(R.id.tvLSHScore)).getText().toString());
        int ascore = Integer.valueOf(((TextView)findViewById(R.id.tvLSAScore)).getText().toString());

        form.put("id",sharedPref.getInt("game_id",0));
        form.put("team_1",hteam);
        form.put("score_1",hscore);
        form.put("team_2",ateam);
        form.put("score_2",ascore);

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
                            Log.d("live_score","Game ended Successful!");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Game ending failed!", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(UpdateLiveScore.this,Admin_Dashboard.class);
        startActivity(intent);
        finish();
    }
}
