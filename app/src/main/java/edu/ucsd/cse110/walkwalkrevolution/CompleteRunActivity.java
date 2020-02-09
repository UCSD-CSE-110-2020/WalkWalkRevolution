package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompleteRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_run);

        Button bt_enterInfo1 = (Button) findViewById(R.id.bt_gotoEnterInfo1);

        // check if user pressed next
        bt_enterInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEnterInfo1();
            }
        });
    }

    public void gotoEnterInfo1() {
        Intent intent = new Intent(this, EnterInfo1Activity.class);
        startActivity(intent);
    }
}
