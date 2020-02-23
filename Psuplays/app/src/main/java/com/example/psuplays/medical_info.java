package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class medical_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_info);
    }

    public void call_submit(View view) {
        Toast.makeText(medical_info.this,"Sign Up Successfull",Toast.LENGTH_SHORT).show();
        finish();
    }
}
