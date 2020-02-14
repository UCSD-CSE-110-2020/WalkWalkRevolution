package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class CurrentWalkActivity extends AppCompatActivity {

    TextView currTime;
    long startTime;
    long countUp;
    int iniStep = 0;
    double iniDistance = 0;
    double height = 5.75;
    double stepMultiplier = 0.414;
    double footInMile = 5280.00;
    Walk newWalk = new Walk("0.0", iniStep, iniDistance);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_walk);

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();

        currTime = (TextView) findViewById(R.id.box_currTime);
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);
                newWalk.setSteps(iniStep);
                newWalk.setTotalTime(asText);

                iniDistance = ((double)iniStep * height * stepMultiplier) / footInMile;
                newWalk.setDistance(round(iniDistance, 3));

                TextView updateStep = findViewById(R.id.box_currSteps);
                TextView updateDistance = findViewById(R.id.box_currDist);
                updateStep.setText(Integer.toString(iniStep++));
                updateDistance.setText(round(iniDistance, 3) + " miles");

                currTime.setText(asText);
            }
        });
        stopWatch.start();

        // check if user pressed stop button
        bt_stopRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRoute();
            }
        });

        Button bt_mock = (Button) findViewById(R.id.bt_mock);
        // support mocking
        bt_mock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMock();
            }
        });
    }

    public void gotoNewRoute() {
        Intent intent = new Intent(this, RouteNewActivity.class);
        intent.putExtra("finalWalk", newWalk);
        finish();
        startActivity(intent);
    }

    public void gotoMock() {
        Intent intent = new Intent(this, RouteNewActivity.class);
        startActivity(intent);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
