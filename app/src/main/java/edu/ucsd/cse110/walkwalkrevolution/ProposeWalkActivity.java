package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProposeWalkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_walk);

        // Check if user pressed send button
        Button bt_send = (Button) findViewById(R.id.bt_sendProposeWalk);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // day and time must be entered
                if (((EditText) findViewById(R.id.box_day)).getText().toString().matches("")) {
                    Toast.makeText(ProposeWalkActivity.this, "Please enter a day", Toast.LENGTH_SHORT).show();
                }
                else if (((EditText) findViewById(R.id.box_time)).getText().toString().matches("")) {
                    Toast.makeText(ProposeWalkActivity.this, "Please enter a time", Toast.LENGTH_SHORT).show();
                }
                else {
                    save();
                    gotoMainMenu();
                }
            }
        });
    }


    public void save() {
        EditText eDay = (EditText) findViewById(R.id.box_day);
        EditText eTime = (EditText) findViewById(R.id.box_time);

        String day = eDay.getText().toString();
        String time = eTime.getText().toString();

    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        startActivity(intentMainMenu);
    }
}
