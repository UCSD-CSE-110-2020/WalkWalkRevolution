package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class CurrentRunActivity extends AppCompatActivity {

    TextView textGoesHere;
    long startTime;
    long countUp;
    int iniStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_run);

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);
        Walk newWalk = new Walk("0:0", 0, 0, 0.0);
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();

        textGoesHere = (TextView) findViewById(R.id.textGoesHere);
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);
                newWalk.setTotalTime(asText);

                if ((countUp % 60) % 5 == 0 ) {
                    TextView updateStep = findViewById(R.id.textView16);
                    updateStep.setText(Integer.toString((iniStep++)));
                }
                textGoesHere.setText(asText);
            }
        });
        stopWatch.start();

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
