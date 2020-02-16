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

    String name = "NEW WALK"; // Save the title of walk
    TextView currTime; // Save the current time
    long startTime; // Save the start time
    long countUp; // Count the seconds up to 60
    int iniStep = 0; // Initial step
    double iniDistance = 0; // Initial distance
    float height = 0; // Initial height
    double stepMultiplier = 0.414; // Multiplier for distance calculation
    double footInMile = 5280.00; // Used for conversion to mile
    Walk newWalk = new Walk("0.0", iniStep, iniDistance); // Walk object to save information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_walk);


        // Get the user height, and convert it to the foot
        Intent i = getIntent();
        height = (float)i.getSerializableExtra("savedHeight");
        height = height / 12;

        // set title name
        String tempName = (String)i.getSerializableExtra("title");
        if(tempName != null) {
            name = tempName;
        }
        TextView title = (TextView) findViewById(R.id.title_routeName);
        title.setText(name);

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);

        // Set up the chronometer to keep track of the time
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();
        currTime = (TextView) findViewById(R.id.box_currTime);
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            @Override
            // Every tick, it will update the information
            // Credit to Steve Haley,
            // https://stackoverflow.com/questions/2536882/create-an-incrementing-timer-in-
            // seconds-in-0000-format
            public void onChronometerTick(Chronometer arg0) {

                // Format the timer for user interface
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);

                // Record the step and total time, and save them to the walk object
                newWalk.setSteps(iniStep);
                newWalk.setTotalTime(asText);

                // Calculate the distance in mile, and save it to the walk object
                iniDistance = ((double)iniStep * height * stepMultiplier) / footInMile;
                newWalk.setDistance(round(iniDistance, 3));

                // Update the user interface accordingly
                TextView updateStep = findViewById(R.id.box_currSteps);
                TextView updateDistance = findViewById(R.id.box_currDist);
                updateStep.setText(Integer.toString(iniStep++));
                updateDistance.setText(round(iniDistance, 3) + " miles");

                // Update the timer
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
        Intent intent = new Intent(this, MockActivity.class);
        startActivity(intent);
    }

    // Round the double to declared position.
    // Credit to Jonik, https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
