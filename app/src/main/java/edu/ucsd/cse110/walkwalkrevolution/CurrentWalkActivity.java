package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Clock;
import java.util.Calendar;

public class CurrentWalkActivity extends AppCompatActivity {

    String name = "NEW WALK"; // Save the title of walk
    Route savedRoute = null; // Saved route
    TextView currTime; // Save the current time
    long startTime; // Save the start time
    long countUp; // Count the seconds up to 60
    int iniStep = 0; // Initial step
    float iniDistance = 0; // Initial distance
    float height = 0; // Initial height
    Walk newWalk = new Walk("0.0", iniStep, iniDistance); // Walk object to save information
    boolean isSavedRoute = false; // Check if current walk is already saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_walk);

        // Get the user height, and convert it to the foot
        Intent i = getIntent();
        height = (float)i.getSerializableExtra("savedHeight");

        isSavedRoute = (boolean)i.getSerializableExtra("isSavedRoute");

        // set title name
        String tempName = (String)i.getSerializableExtra("title");
        if(tempName != null) {
            name = tempName;
        }
        TextView title = (TextView) findViewById(R.id.title_routeName);
        title.setText(name);

        savedRoute = (Route)i.getSerializableExtra("route");

        Button bt_stopRun = (Button) findViewById(R.id.bt_stopRun);

        // Set up the chronometer to keep track of the time
        Intent intent = getIntent();
        Clock clock = (Clock)intent.getSerializableExtra("clock");
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
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
                String asText = (countUp / MeasurementConverter.SECS_IN_MIN) + ":" + (countUp % MeasurementConverter.SECS_IN_MIN);

                // Record the step and total time, and save them to the walk object
                int stepCount = WalkWalkRevolutionApplication.stepCount.get();
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
                gotoNewRoute();

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
