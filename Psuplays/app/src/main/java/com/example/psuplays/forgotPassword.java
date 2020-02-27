package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class forgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ((TextView)findViewById(R.id.etEmail)).setText(getIntent().getExtras().getString("username"));
    }

    public void changePassword(View view) {

        String username = ((TextView)findViewById(R.id.etEmail)).getText().toString();
        String password = ((TextView)findViewById(R.id.etPassword)).getText().toString();
        String re_password = ((TextView)findViewById(R.id.etRePassword)).getText().toString();
        String answer1 = ((TextView)findViewById(R.id.etAnswer1)).getText().toString();
        String answer2 = ((TextView)findViewById(R.id.etAnswer2)).getText().toString();

        if(username.isEmpty()){
            Toast.makeText(forgotPassword.this, "Username is empty", Toast.LENGTH_SHORT).show();
        }

        else if(password.isEmpty()){
            Toast.makeText(forgotPassword.this, "Password is empty", Toast.LENGTH_SHORT).show();
        }

        else if(re_password.isEmpty()){
            Toast.makeText(forgotPassword.this, "Re-enter Password is empty", Toast.LENGTH_SHORT).show();
        }

        else if(answer1.isEmpty()||answer2.isEmpty()){
            Toast.makeText(forgotPassword.this, "Enter answer for security question(s).", Toast.LENGTH_SHORT).show();
        }

        else if(!(password.equals(re_password))){
            Toast.makeText(forgotPassword.this, "Password does not match", Toast.LENGTH_SHORT).show();
        }

        else {
            String[] temp = username.split("@");
            if((temp.length < 2)||!(temp[1].equals("psu.edu"))) {
                Toast.makeText(forgotPassword.this, "Not a valid username", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(forgotPassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}
