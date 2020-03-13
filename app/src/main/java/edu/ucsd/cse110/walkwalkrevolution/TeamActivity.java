package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // Check if user pressed home button
        Button bt_mainMenu = (Button) findViewById(R.id.bt_home2);
        bt_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainMenu();
            }
        });

        // Check if user pressed add team member button
        FloatingActionButton bt_addTeamMember = (FloatingActionButton) findViewById(R.id.bt_addTeamMember);
        bt_addTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddTeamMember();
            }
        });

        // Setup RouteManager
        MembersManager manager = new MembersManager(this);

        // Load Route List
        ListView list = (ListView) findViewById(R.id.teamList);

        manager.load(WalkWalkRevolutionApplication.adapter, list);
    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMainMenu);
    }

    public void gotoAddTeamMember() {
        Intent intentAddTeamMember = new Intent(this, AddTeamMemberActivity.class);
        startActivity(intentAddTeamMember);
    }
}
