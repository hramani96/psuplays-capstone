package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class forgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ((TextView)findViewById(R.id.etEmail)).setText(getIntent().getExtras().getString("username"));
    }

    public void changePassword(View view) throws JSONException {

        String username = ((TextView)findViewById(R.id.etEmail)).getText().toString();
        String password = ((TextView)findViewById(R.id.etPassword)).getText().toString();
        String re_password = ((TextView)findViewById(R.id.etRePassword)).getText().toString();
        String answer1 = ((TextView)findViewById(R.id.etAnswer1)).getText().toString();
        String answer2 = ((TextView)findViewById(R.id.etAnswer2)).getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/forgotPassword/";
        JSONObject form = new JSONObject();
        form.put("email",username);
        form.put("password",password);
        form.put("password_conf",re_password);
        form.put("ans1",answer1);
        form.put("ans2",answer2);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Sign up", response.toString());
                        String status = "";
                        try {
                            status = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Toast.makeText(forgotPassword.this,"Password Changed successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(forgotPassword.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(forgotPassword.this, "Password changed failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(forgotPassword.this,message, Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);

    }
}
