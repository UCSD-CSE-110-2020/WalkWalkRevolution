package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CurrentRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_run);

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);

        // check if user pressed stop button
        bt_stopRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCompleteRun();
            }
        });
    }

    public void gotoCompleteRun() {
        Intent intent = new Intent(this, CompleteRunActivity.class);
        startActivity(intent);
    }
}
