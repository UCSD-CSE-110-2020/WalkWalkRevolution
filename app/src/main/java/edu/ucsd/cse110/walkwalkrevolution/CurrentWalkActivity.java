package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Clock;
import java.util.Calendar;

public class CurrentWalkActivity extends AppCompatActivity {

    private String name = "NEW WALK"; // Save the title of walk
    private Route savedRoute = null; // Saved route
    private TextView currTime; // Save the current time
    private long countUp; // Count the seconds up to 60
    private int iniStep = 0; // Initial step
    private float iniDistance = 0; // Initial distance
    private float height = 0; // Initial height
    private Walk newWalk = new Walk("0.0", iniStep, iniDistance); // Walk object to save information
    private boolean isSavedRoute = false; // Check if current walk is already saved

    private static Clock clock;
    private static Chronometer stopWatch;

    private static final String TAG = "CurrentWalkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_walk);

        Log.d(TAG, "Creating current walk activity");

        // Get the user height, and convert it to the foot
        Intent intent = getIntent();
        height = intent.getFloatExtra("savedHeight", -1);
        isSavedRoute = intent.getBooleanExtra("isSavedRoute", false);
        savedRoute = (Route)intent.getSerializableExtra("route");
        if (intent.hasExtra("title")) {
            name = intent.getStringExtra("title");
        }

        TextView title = (TextView) findViewById(R.id.title_routeName);
        title.setText(name);

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);

        // Default case is to use a clock that pulls from the default time zone
        if (intent.hasExtra("clock")) {
            clock = (Clock) intent.getSerializableExtra("clock");
        } else if (clock == null) {
            clock = Clock.systemDefaultZone();
        }

        iniStep = WalkWalkRevolutionApplication.stepCount.get();

        // Set up the chronometer to keep track of the time
        stopWatch = (Chronometer) findViewById(R.id.chrono);
        currTime = (TextView) findViewById(R.id.box_currTime);
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            // Every tick, it will update the information
            // Credit to Steve Haley,
            // https://stackoverflow.com/questions/2536882/create-an-incrementing-timer-in-
            // seconds-in-0000-format
            @Override
            public void onChronometerTick(Chronometer arg0) {
                // Format the timer for user interface
                countUp = ((clock.millis() - arg0.getBase()) / MeasurementConverter.MILLIS_IN_SEC);
                long minutes = countUp / MeasurementConverter.SECS_IN_MIN;
                long seconds = countUp % MeasurementConverter.SECS_IN_MIN;
                String asText = (minutes / 10 == 0 ? "0" + minutes : minutes) + ":" + (seconds / 10 == 0 ? "0" + seconds : seconds) ;

                // Record the step and total time, and save them to the walk object
                int stepCount = WalkWalkRevolutionApplication.stepCount.get() - iniStep;
                if (stepCount < 0) {
                    iniStep = WalkWalkRevolutionApplication.stepCount.get();
                }
                newWalk.setSteps(stepCount);
                newWalk.setTotalTime(asText);

                // Calculate the distance in mile, and save it to the walk object
                iniDistance = MeasurementConverter.stepToMiles(stepCount, height);
                newWalk.setDistance(Float.parseFloat(String.format("%.1f", iniDistance)));

                // Update the user interface accordingly
                TextView updateStep = findViewById(R.id.box_currSteps);
                TextView updateDistance = findViewById(R.id.box_currDist);
                updateStep.setText(String.valueOf(stepCount));
                updateDistance.setText(String.format("%.1f miles", iniDistance));

                // Update the timer
                currTime.setText(asText);
            }
        });
        stopWatch.setBase(clock.millis());
        stopWatch.start();

        // check if user pressed stop button
        bt_stopRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWatch.stop();
              
                // record last walk and display on home screen
                SharedPreferences lastWalk  = getSharedPreferences("lastWalk", MODE_PRIVATE);
                SharedPreferences.Editor lastWalkEdit = lastWalk.edit();
                lastWalkEdit.putString("steps", String.valueOf(newWalk.getSteps()));
                lastWalkEdit.putString("distance", String.format("%.1f", newWalk.getDistance()));
                lastWalkEdit.putString("time", newWalk.getTotalTime());
                lastWalkEdit.apply();

                // if it is a saved route then go to routes screen
                // if it not saved then go to new route screen
                if(!isSavedRoute)
                    gotoNewRoute();
                else
                    gotoRoutes();
            }
        });

        // Support mocking
        Button bt_mock = (Button) findViewById(R.id.bt_mock);
        bt_mock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMock();
            }
        });
    }

    public static void setClock(Clock c) {
        clock = c;

        // Restart the stopwatch using the new clock
        stopWatch.stop();
        stopWatch.setBase(clock.millis());
        stopWatch.start();
    }

    public void gotoNewRoute() {
        Intent intent = new Intent(this, RouteNewActivity.class);
        intent.putExtra("finalWalk", newWalk);
        intent.putExtra("manuallyAddNewRoute", false);
        finish();
        startActivity(intent);
    }

    public void gotoRoutes() {
        // change data of saved route
        RoutesManager manager = new RoutesManager(this);
        Route tempRoute = savedRoute;
        manager.deleteRoute(name);
        Calendar rightNow = Calendar.getInstance();
        tempRoute.setLastRun(rightNow);
        tempRoute.setSteps(newWalk.getSteps());
        tempRoute.setDistance(newWalk.getDistance());
        manager.addRoute(tempRoute);
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

    public void gotoMock() {
        Intent intent = new Intent(this, MockActivity.class);
        startActivity(intent);
    }
}
