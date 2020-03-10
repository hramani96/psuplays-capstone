package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void tryresponse(String uname,  String Password) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:71.114.127.203:8888/dashboard/";
        JSONObject form = new JSONObject();
        form.put("email",uname);
        form.put("password",Password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Toast.makeText(MainActivity.this,"Signin successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Signin not successfull", Toast.LENGTH_SHORT).show();
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

        tryresponse(uname,password);
    }
}
