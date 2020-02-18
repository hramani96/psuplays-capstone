package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
        Toast.makeText(forgotPassword.this,"Password Changed",Toast.LENGTH_SHORT).show();
        finish();
    }
}
