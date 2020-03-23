package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class medical_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_info);
    }


    public void call_submit(View view) {

        String height = ((TextView)findViewById(R.id.etHeight)).getText().toString();
        String weight = ((TextView)findViewById(R.id.etWeight)).getText().toString();
        String med_desc = ((TextView)findViewById(R.id.etMedical_condition)).getText().toString();
        String temp = ((TextView)findViewById(R.id.etBirth_date)).getText().toString();
        String[] birth_date = temp.split("/");
        String ec_name = ((TextView)findViewById(R.id.etName)).getText().toString();
        String ec_number = ((TextView)findViewById(R.id.etNumber)).getText().toString();

        if(height.isEmpty()||weight.isEmpty()||temp.isEmpty()||ec_name.isEmpty()||ec_number.isEmpty()){
            Toast.makeText(medical_info.this,"Filed with (*) cannot be Empty.",Toast.LENGTH_SHORT).show();
        }

        else if(!(birth_date.length == 3)){
            Toast.makeText(medical_info.this,"Invalid Date format.",Toast.LENGTH_SHORT).show();
        }

        else if((Integer.parseInt(birth_date[0]) > 12) || (Integer.parseInt(birth_date[0]) > 12) || (Integer.parseInt(birth_date[0]) > 12))

            Toast.makeText(medical_info.this,"Sign Up Successfull",Toast.LENGTH_SHORT).show();
        finish();
    }
}
