package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
}
