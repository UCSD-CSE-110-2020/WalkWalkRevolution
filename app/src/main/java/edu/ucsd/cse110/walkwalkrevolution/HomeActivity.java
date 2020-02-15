package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Properties;

import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkwalkrevolution.fitness.GoogleFitAdapter;


public class HomeActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String FITNESS_SERVICE_PROPERTIES = "fitness.properties";

    private static final String TAG = "HomeActivity";

    private int lastStepCount = 0;

    private TextView textSteps;
    private FitnessService fitnessService;
    private StepCountUpdateAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSteps = findViewById(R.id.textSteps);

        // Read the fitness key from the fitness properties file
        PropertyReader propertyReader = new PropertyReader(this);
        Properties properties = propertyReader.getMyProperties(FITNESS_SERVICE_PROPERTIES);
        String fitnessServiceKey = properties.getProperty(FITNESS_SERVICE_KEY);

        // Create the fitness service using the factory
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomeActivity HomeActivity) {
                return new GoogleFitAdapter(HomeActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        if (task == null) {
            task = new StepCountUpdateAsyncTask();
            task.execute();
        }

        Button bt_newRun = (Button) findViewById(R.id.bt_startNewRun);

        // Check if user pressed start new run button
        bt_newRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRun();
            }
        });

        Button bt_routes = (Button) findViewById(R.id.bt_gotoRoute2);

        // Check if user pressed routes button
        bt_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRoutes();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    public void gotoNewRun() {
        Intent intent = new Intent(this, CurrentRunActivity.class);
        startActivity(intent);
    }

    public void gotoRoutes() {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

    public void setStepCount(int stepCount) {
        lastStepCount = stepCount;
        textSteps.setText(String.valueOf(stepCount));
    }

    public long getStepCount() {
        fitnessService.updateStepCount();
        return lastStepCount;
    }

    private class StepCountUpdateAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            while (!isCancelled()) {
                fitnessService.updateStepCount();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}

