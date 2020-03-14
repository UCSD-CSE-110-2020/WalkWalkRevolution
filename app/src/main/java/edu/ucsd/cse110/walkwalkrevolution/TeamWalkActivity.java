package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class TeamWalkActivity extends AppCompatActivity {
    private static final String TAG = TeamWalkActivity.class.getSimpleName();
    TextView boxName, boxDate, boxLocation, boxStatus, nameLocation;
    ListView responseList;
    Button bt_accept, bt_timeDecline, bt_routeDecline, bt_schedule, bt_withdraw;
    User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_walk);
        boxName = (TextView) findViewById(R.id.box_teamWalkName);
        boxDate = (TextView) findViewById(R.id.box_teamWalkDate);
        boxLocation = (TextView) findViewById(R.id.box_teamWalkLocation);
        boxStatus = (TextView) findViewById(R.id.box_teamWalkStatus);
        nameLocation = (TextView) findViewById(R.id.name_teamWalkLocation);
        responseList = (ListView) findViewById(R.id.teamWalkList);
        bt_accept = (Button) findViewById(R.id.bt_acceptTeamWalk);
        bt_timeDecline = (Button) findViewById(R.id.bt_timeDeclineTeamWalk);
        bt_routeDecline = (Button) findViewById(R.id.bt_routeDeclineTeamWalk);
        bt_schedule = (Button) findViewById(R.id.bt_scheduleTeamWalk);
        bt_withdraw = (Button) findViewById(R.id.bt_withdrawTeamWalk);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String uid = user.getUid();
        appUser = new User(name, email, uid);

        // Check if user pressed home button
        Button bt_mainMenu = (Button) findViewById(R.id.bt_home);
        bt_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainMenu();
            }
        });
        checkIfWalkExists();

        // Setup RouteManager
//        MembersManager manager = new MembersManager(this);
//
//        // Load Route List
//        ListView list = (ListView) findViewById(R.id.teamWalkList);
//
//        manager.load(WalkWalkRevolutionApplication.adapter, list);

    }

    private void hideAll() {
        boxName.setVisibility(View.GONE);
        boxDate.setVisibility(View.GONE);
        boxLocation.setVisibility(View.GONE);
        findViewById(R.id.name_teamWalkStatus).setVisibility(View.GONE);
        boxStatus.setVisibility(View.GONE);
        responseList.setVisibility(View.GONE);
        bt_accept.setVisibility(View.GONE);
        bt_timeDecline.setVisibility(View.GONE);
        bt_routeDecline.setVisibility(View.GONE);
        bt_schedule.setVisibility(View.GONE);
        bt_withdraw.setVisibility(View.GONE);
    }

    private void checkIfWalkExists() {
        String teamId = Team.getTeam(this);

        if (teamId.equals(getResources().getString(R.string.empty))) { // If team does not exist, hide everything
            Log.d(TAG, "Not part of a team, hiding everything.");
            hideAll();
            nameLocation.setText("Not currently in a team!");
            nameLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
//        } else {
//            String[] ids = {"users", appUser.getName() + " " + appUser.getUid()};
//            DocumentReference docRef = WalkWalkRevolutionApplication.adapter.get(ids);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d(TAG, "User '" + java.util.Arrays.toString(ids) + "' exists, checking if invited.");
//                            if (document.get("invite") == null) {
//                                bt_seeMyInvitation.setVisibility(View.INVISIBLE);
//                                Log.d(TAG, "User does not have an invitation, hiding button.");
//                            }
//                            else {
//                                bt_seeMyInvitation.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        gotoAcceptInvitation();
//                                    }
//                                });
//                            }
//                        } else {
//                            Log.d(TAG, "User '" + java.util.Arrays.toString(ids) + "' does not exist, not checking if invited.");
//                        }
//                    }
//                }
//            });
        }
    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMainMenu);
    }
}

