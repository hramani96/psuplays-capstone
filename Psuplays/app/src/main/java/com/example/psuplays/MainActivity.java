package com.example.psuplays;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);

        Boolean cred = sharedPref.getBoolean("remember_credentials",false);
        if(cred){
            String username = sharedPref.getString("username","");
            String password = sharedPref.getString("password","");
            ((TextView)findViewById(R.id.etUsername)).setText(username);
            ((TextView)findViewById(R.id.etPassword)).setText(password);
        }

        Boolean pref = sharedPref.getBoolean("log_in_preference",false);
        if(pref){
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Logging in! Please wait ....");
            pd.show();
            String username = sharedPref.getString("username","");
            String password = sharedPref.getString("password","");
            String role = sharedPref.getString("role","");
            Log.d("values",username +" " + password + " " + role);
            try {
                tryresponse(username,password,role);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void signupPage(View view) {
        Intent intent = new Intent(MainActivity.this, signup.class);
        startActivity(intent);
    }

    public void fgtPsswdPage(View view) {
        Intent intent = new Intent(MainActivity.this, forgotPassword.class);
        intent.putExtra("username",(((TextView)findViewById(R.id.etUsername)).getText()).toString());
        startActivity(intent);
    }

    public void tryresponse(final String uname, final String Password, final String role) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888" + role;
        JSONObject form = new JSONObject();
        form.put("email",uname);
        form.put("password",Password);

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
                            Toast.makeText(MainActivity.this,"Sign-in successful",Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("username",uname);
                            editor.putString("password",Password);
                            editor.commit();
                            if(role.equals("/loginStudent/")) {
                                editor.putString("role","/loginStudent/");
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, Student_Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                            if(role.equals("/loginAdmin/")) {
                                editor.putString("role","/loginAdmin/");
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, Admin_Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Sign-in not successful", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);
    }

    public void studentDash(View view) throws JSONException {
        String uname = ((TextView)findViewById(R.id.etUsername)).getText().toString();
        String password = ((TextView)findViewById(R.id.etPassword)).getText().toString();

        //Intent intent = new Intent(MainActivity.this,Student_Dashboard.class);
        //startActivity(intent);
        tryresponse(uname,password,"/loginStudent/");
    }

    public void adminDash(View view) throws JSONException {
        String uname = ((TextView)findViewById(R.id.etUsername)).getText().toString();
        String password = ((TextView)findViewById(R.id.etPassword)).getText().toString();

        //Intent intent = new Intent(MainActivity.this,Admin_Dashboard.class);
        //startActivity(intent);
        tryresponse(uname,password,"/loginAdmin/");
    }
}
