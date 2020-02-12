package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrentRunActivity extends AppCompatActivity {

    TextView textGoesHere;
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
        setContentView(R.layout.activity_current_run);

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();

        textGoesHere = (TextView) findViewById(R.id.textGoesHere);
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);
                newWalk.setSteps(iniStep);
                newWalk.setTotalTime(asText);

                iniDistance = ((double)iniStep * height * stepMultiplier) / footInMile;
                newWalk.setDistance(round(iniDistance, 3));

                TextView updateStep = findViewById(R.id.textView16);
                TextView updateDistance = findViewById(R.id.textView18);
                updateStep.setText(Integer.toString(iniStep++));
                updateDistance.setText(round(iniDistance, 3) + " miles");

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
        intent.putExtra("finalWalk", newWalk);
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
