package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;


public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void button_click(View view) throws JSONException {
        String email = ((TextView)findViewById(R.id.etEmail)).getText().toString();
        String fname = ((TextView)findViewById(R.id.etFirstName)).getText().toString();
        String lname = ((TextView)findViewById(R.id.etLastName)).getText().toString();
        String password = ((TextView)findViewById(R.id.etPassword)).getText().toString();
        String cpassword = ((TextView)findViewById(R.id.etRePassword)).getText().toString();

        tryresponse(fname,lname,email,password,cpassword);
    }

    public void tryresponse(String fname, String lname, String email, String Password, String cPassword) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:71.114.127.203:8888/add_student/";
        JSONObject form = new JSONObject();
        form.put("first_name",fname);
        form.put("last_name",lname);
        form.put("email",email);
        form.put("password",Password);
        form.put("password_conf",cPassword);

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
                            Toast.makeText(signup.this,"Signup successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(signup.this, medical_info.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(signup.this, "Signup not successfull", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(signup.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
