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

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.walkwalkrevolution.notifications.Notification;

import static java.lang.Thread.sleep;

public class AddTeamMemberActivity extends AppCompatActivity {
    private static final String TAG = AddTeamMemberActivity.class.getSimpleName();
    User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);

        //appUser = (User) getIntent().getSerializableExtra("appUser");
        appUser = new User(WalkWalkRevolutionApplication.adapter, FirebaseAuth.getInstance().getCurrentUser());

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
                    invite(new Callback.NoArg() {
                        @Override
                        public void call() {
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

    public void invite(Callback.NoArg callback) {
        if (Team.teamExists(this)) {
            appUser.createTeam(this);
        }

        EditText eName = (EditText) findViewById(R.id.box_name);
        EditText eEmail = (EditText) findViewById(R.id.box_gmail);
        String iName = eName.getText().toString().trim();
        String iEmail = eEmail.getText().toString().trim().toLowerCase();

        Invite invite = new Invite(this, appUser.getName(), appUser.getEmail(), iName, iEmail);
        Log.d(TAG, "Requesting that invite be added to the database");
        invite.addToDatabase(WalkWalkRevolutionApplication.adapter, callback);
        Notification.sendNotification(WalkWalkRevolutionApplication.adapter, iEmail, "Invitation", appUser.getName() + " sent you an invitation!");
    }
}
