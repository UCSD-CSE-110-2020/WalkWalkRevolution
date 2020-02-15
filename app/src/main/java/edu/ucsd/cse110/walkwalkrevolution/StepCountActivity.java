package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessServiceFactory;


public class StepCountActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private long lastStepCount = 0;

    private TextView textSteps;
    private FitnessService fitnessService;
    private StepCountUpdateAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textSteps = findViewById(R.id.box_dailySteps);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        fitnessService.setup();

        if (task == null) {
            //task = new StepCountUpdateAsyncTask();
            //task.execute();
        }
        fitnessService.updateStepCount();
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

    public void setStepCount(long stepCount) {
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

