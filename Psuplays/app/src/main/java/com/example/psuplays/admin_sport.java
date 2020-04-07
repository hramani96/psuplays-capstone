package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class admin_sport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sport);

        TextView tvsport = findViewById(R.id.tvSportName);
        String sport = getIntent().getStringExtra("sport");
        tvsport.setText(sport);

        setTitle(sport);
    }
}
