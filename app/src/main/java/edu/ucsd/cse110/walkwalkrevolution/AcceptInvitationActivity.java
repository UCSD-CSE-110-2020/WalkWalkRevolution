package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AcceptInvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invitation);

        // check if user pressed accept
        Button bt_accept = (Button) findViewById(R.id.bt_acceptInvitation);
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptInvitation();
                gotoTeamMenu();
            }
        });

        // check if user pressed decline
        Button bt_decline = (Button) findViewById(R.id.bt_declineInvitation);
        bt_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineInvitation();
                gotoTeamMenu();
            }
        });

    }


    public void acceptInvitation() {
        Toast.makeText(AcceptInvitationActivity.this, "Team invitation accepted", Toast.LENGTH_SHORT).show();
        //TODO

    }



    public void declineInvitation() {
        Toast.makeText(AcceptInvitationActivity.this, "Team invitation declined", Toast.LENGTH_SHORT).show();
        //TODO

    }


    public void gotoTeamMenu() {
        Intent intent = new Intent(this, TeamActivity.class);
        startActivity(intent);
    }
}
