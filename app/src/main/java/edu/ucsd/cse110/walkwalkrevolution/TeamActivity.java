package edu.ucsd.cse110.walkwalkrevolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import static java.lang.Thread.sleep;

public class TeamActivity extends AppCompatActivity {
    private static final String TAG = TeamActivity.class.getSimpleName();
    Button bt_seeMyInvitation;
    User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        appUser = User.getUser();

        // Check if user pressed home button
        Button bt_mainMenu = (Button) findViewById(R.id.bt_home);
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

        // Check if user pressed My invitation button
        bt_seeMyInvitation = (Button) findViewById(R.id.bt_myInvitation);
        checkIfInvited();

        // Setup MembersManager
        MembersManager manager = new MembersManager(this);

        // Load Member List
        ListView list = (ListView) findViewById(R.id.teamList);

        manager.load(WalkWalkRevolutionApplication.adapter, list);

    }


    private void checkIfInvited() {
        String teamId = Team.getTeam(this);

        if (!teamId.equals(getResources().getString(R.string.empty))) { // If team exists, hide button
            Log.d(TAG, "Already a part of a team, hiding button.");
        } else {
            String[] ids = {"users", appUser.getName() + " " + appUser.getUid()};
            DocumentReference docRef = WalkWalkRevolutionApplication.adapter.get(ids);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "User '" + java.util.Arrays.toString(ids) + "' exists, checking if invited.");
                            if (document.get("invite") == null) {
                                Log.d(TAG, "User does not have an invitation, hiding button.");
                            }
                            else {
                                bt_seeMyInvitation.setVisibility(View.VISIBLE);
                                bt_seeMyInvitation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gotoAcceptInvitation();
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "User '" + java.util.Arrays.toString(ids) + "' does not exist, not checking if invited.");
                        }
                    }
                }
            });
        }
    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMainMenu);
    }

    public void gotoAddTeamMember() {
        Intent intentAddTeamMember = new Intent(this, AddTeamMemberActivity.class);
        intentAddTeamMember.putExtra("appUser", appUser);
        startActivity(intentAddTeamMember);
    }

    public void gotoAcceptInvitation() {
        Intent intentAcceptInvitation = new Intent(this, AcceptInvitationActivity.class);
        intentAcceptInvitation.putExtra("appUser", appUser);
        startActivity(intentAcceptInvitation);
    }
}
