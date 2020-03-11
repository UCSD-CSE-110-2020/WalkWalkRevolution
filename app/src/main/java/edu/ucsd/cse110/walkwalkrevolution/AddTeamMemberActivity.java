package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTeamMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);

        // Check if user pressed add button
        Button bt_finishAddMember = (Button) findViewById(R.id.bt_finishAddMember);

        // check if user pressed next
        bt_finishAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // name and emial must be entered
                if (((EditText) findViewById(R.id.box_name)).getText().toString().matches("")) {
                    Toast.makeText(AddTeamMemberActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
                else if (((EditText) findViewById(R.id.box_gmail)).getText().toString().matches("")) {
                    Toast.makeText(AddTeamMemberActivity.this, "Please enter Gmail address", Toast.LENGTH_SHORT).show();
                }
                else {
                    save();
                    gotoTeamMenu();
                }
            }
        });
    }

    public void gotoTeamMenu() {
        Intent intent = new Intent(this, TeamActivity.class);
        startActivity(intent);
    }

    public void save() {
        EditText eName = (EditText) findViewById(R.id.box_name);
        EditText eGmail = (EditText) findViewById(R.id.box_gmail);

        String name = eName.getText().toString();
        String gmail = eGmail.getText().toString();

    }
}
