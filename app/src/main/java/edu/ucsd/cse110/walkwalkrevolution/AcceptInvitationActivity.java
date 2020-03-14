package edu.ucsd.cse110.walkwalkrevolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.notifications.Notification;

import static java.lang.Thread.sleep;

public class AcceptInvitationActivity extends AppCompatActivity {
    private static final String TAG = AcceptInvitationActivity.class.getSimpleName();

    User appUser;
    DocumentReference userRef;
    String[] userIds;
    String inviterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invitation);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String uid = user.getUid();
        appUser = new User(name, email, uid);

        userIds = new String[]{"users", appUser.getName() + " " + appUser.getUid()};
        userRef = WalkWalkRevolutionApplication.adapter.get(userIds);
        TextView header_inviter = (TextView) findViewById(R.id.header_nameOfInviter);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "User '" + java.util.Arrays.toString(userIds) + "' exists, getting inviter.");
                        Map invite = (Map) document.get("invite");
                        header_inviter.setText((String) invite.get("from"));
                        saveInviterEmail((String) invite.get("email"));
                    } else {
                        Log.d(TAG, "User '" + java.util.Arrays.toString(userIds) + "' does not exist, cannot get inviter.");
                    }
                }
            }
        });

        // check if user pressed accept
        Button bt_accept = (Button) findViewById(R.id.bt_acceptInvitation);
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptInvitation(new Callback() {
                    @Override
                    public void onCallback() {
                        gotoTeamMenu();
                    }
                });
            }
        });

        // check if user pressed decline
        Button bt_decline = (Button) findViewById(R.id.bt_declineInvitation);
        bt_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineInvitation(new Callback() {
                    @Override
                    public void onCallback() {
                        gotoTeamMenu();
                    }
                });
            }
        });

    }

    public void saveInviterEmail(String email) {
        inviterEmail = email;
    }

    public void acceptInvitation(Callback callback) {
        Toast.makeText(AcceptInvitationActivity.this, "Team invitation accepted", Toast.LENGTH_SHORT).show();
        Notification.sendNotification(WalkWalkRevolutionApplication.adapter, inviterEmail, "Invitation Response", appUser.getName() + " accepted your team invitation!");
        respondToInvitation(true, callback);
    }

    public void declineInvitation(Callback callback) {
        Notification.sendNotification(WalkWalkRevolutionApplication.adapter, inviterEmail, "Invitation Response", appUser.getName() + " declined your team invitation!");
        Toast.makeText(AcceptInvitationActivity.this, "Team invitation declined", Toast.LENGTH_SHORT).show();
        respondToInvitation(false, callback);
    }

    public void respondToInvitation(boolean accepted, Callback callback) {
        Map<String, Object> remove = new HashMap<>();
        remove.put("invite", FieldValue.delete());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User '" + java.util.Arrays.toString(userIds) + "' does exist, getting invitation from it.");
                    DocumentSnapshot document = task.getResult();
                    Map<String, String> invite = (Map) document.get("invite");
                    String teamId = invite.get("teamId");
                    removeFromTeam(teamId, accepted, callback);
                    WalkWalkRevolutionApplication.adapter.add(userIds, remove);
                }
            }
        });
    }

    private void removeFromTeam(String teamId, boolean accepted, Callback callback) {
        String[] teamIds = {"teams", teamId};
        if (accepted) {
            storeTeam(teamId);
        }
        DocumentReference docRef = WalkWalkRevolutionApplication.adapter.get(teamIds);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Team '" + java.util.Arrays.toString(teamIds) + "' exists, getting list of invited people.");
                    Map invited = (Map) document.get("invited");
                    String nickname = (String) invited.get(appUser.getEmail());
                    invited.put(appUser.getEmail(), FieldValue.delete());
                    Map<String, Object> data = new HashMap<>();
                    data.put("invited", invited);
                    if (accepted) {
                        Map members = (Map) document.get("members");
                        members.put(appUser.getEmail(), nickname);
                        data.put("members", members);
                        Log.d(TAG, "Putting into members.");
                    }
                    WalkWalkRevolutionApplication.adapter.add(teamIds, data);
                    Log.d(TAG, "Removing from invited.");
                    callback.onCallback();
                }
            }
        });
    }

    private void storeTeam(String teamId) {
        SharedPreferences teamSp = getSharedPreferences(getResources().getString(R.string.team_store), MODE_PRIVATE);
        SharedPreferences.Editor teamSpEdit = teamSp.edit();
        teamSpEdit.putString("teamId", teamId);
        Log.d(TAG, "Storing teamId into shared preferences.");
        teamSpEdit.commit();
    }

    public void gotoTeamMenu() {
        Intent intent = new Intent(this, TeamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
