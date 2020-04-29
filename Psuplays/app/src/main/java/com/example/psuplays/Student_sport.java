package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Student_sport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sport);
        String sport = getIntent().getStringExtra("sport");

        setTitle(sport);
    }
}
