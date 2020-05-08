package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TeamPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_page);
        String team = getIntent().getExtras().getString("team");
        Boolean member = getIntent().getExtras().getBoolean("member");
        setTitle(team);
        ((TextView)findViewById(R.id.tvRoaster)).setText(team + "'s Roaster:");
        if(member){
            findViewById(R.id.tvAddPlayer).setVisibility(View.VISIBLE);
            findViewById(R.id.btAddPlayer).setVisibility(View.VISIBLE);
            findViewById(R.id.etPlayerEmail).setVisibility(View.VISIBLE);
        }
    }
}
