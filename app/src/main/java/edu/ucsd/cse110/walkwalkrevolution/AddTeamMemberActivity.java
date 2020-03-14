package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.lang.Thread.sleep;

public class AddTeamMemberActivity extends AppCompatActivity {
    private static final String TAG = AddTeamMemberActivity.class.getSimpleName();
    User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String uid = user.getUid();
        appUser = new User(name, email, uid);

        // Check if user pressed add button
        Button bt_finishAddMember = (Button) findViewById(R.id.bt_finishAddMember);

        // check if user pressed next
        bt_finishAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // name and email must be entered
                if (((EditText) findViewById(R.id.box_name)).getText().toString().matches("")) {
                    Toast.makeText(AddTeamMemberActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
                else if (((EditText) findViewById(R.id.box_gmail)).getText().toString().matches("")) {
                    Toast.makeText(AddTeamMemberActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "Starting invite with valid name and email address");
                    invite(new Callback() {
                        @Override
                        public void onCallback() {
                            gotoTeamMenu();
                        }
                    });
                }
            }
        });
    }

    public void gotoTeamMenu() {
        Intent intent = new Intent(this, TeamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void invite(Callback callback) {
        checkIfTeamExists();

        EditText eName = (EditText) findViewById(R.id.box_name);
        EditText eEmail = (EditText) findViewById(R.id.box_gmail);

        String iName = eName.getText().toString().trim();
        String iEmail = eEmail.getText().toString().trim().toLowerCase();
        Invite invite = new Invite(this, appUser.getName(), iName, iEmail);
        Log.d(TAG, "Requesting that invite be added to the database");
        invite.addToDatabase(WalkWalkRevolutionApplication.adapter, callback);
    }

    private void checkIfTeamExists() {
        SharedPreferences teamSp = getSharedPreferences(getResources().getString(R.string.team_store), MODE_PRIVATE);
        SharedPreferences.Editor teamSpEdit = teamSp.edit();

        if (!teamSp.contains("teamId")) {
            createTeam(teamSpEdit);
        }
    }

    private void createTeam(SharedPreferences.Editor teamSpEdit) {
        Team team = new Team(appUser);
        String teamId = team.addToDatabase(WalkWalkRevolutionApplication.adapter);
        teamSpEdit.putString("teamId", teamId);
        teamSpEdit.commit();

        // appUser.addTeamToDatabase(WalkWalkRevolutionApplication.adapter, teamId);
    }

}
