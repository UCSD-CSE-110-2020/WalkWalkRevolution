package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
    public static final String FITNESS_SERVICE_PROPERTIES = "fitness.properties";

    private static final String TAG = "HomeActivity";

    private boolean receiversRegistered;

    private TextView textSteps;
    private FitnessService fitnessService;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fitnessService.updateStepCount();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showHeightDialog();

        textSteps = findViewById(R.id.box_dailySteps);

        // Check if user pressed start new run button
        Button bt_newRun = (Button) findViewById(R.id.bt_startNewWalk);
        bt_newRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRun();
            }
        });

        // Check if user pressed routes button
        Button bt_routes = (Button) findViewById(R.id.bt_routes);
        bt_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        registerReceivers();
        launchFitnessActivity();
        launchUpdateService();
    }

    public void registerReceivers() {
        if (!receiversRegistered) {
            // Register broadcast receiver from step count updater
            registerReceiver(broadcastReceiver, new IntentFilter(StepCountUpdateService.BROADCAST_ACTION));
            receiversRegistered = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiversRegistered) {
            unregisterReceiver(broadcastReceiver);
            receiversRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        registerReceivers();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        registerReceivers();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiversRegistered) {
            unregisterReceiver(broadcastReceiver);
            receiversRegistered = false;
        }
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

    public void launchFitnessActivity() {
        // Read the fitness key from the fitness properties file
        PropertyReader propertyReader = new PropertyReader(this, FITNESS_SERVICE_PROPERTIES);
        Properties properties = propertyReader.get();
        String fitnessServiceKey = properties.getProperty(FITNESS_SERVICE_KEY);

        // Create the fitness service using the factory
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomeActivity HomeActivity) {
                return new GoogleFitAdapter(HomeActivity);
            }
        });

        // If a fitness service key is passed in, it has higher priority (for testing)
        Intent intent = getIntent();
        if (intent.hasExtra(FITNESS_SERVICE_KEY)) {
            fitnessServiceKey = intent.getStringExtra(FITNESS_SERVICE_KEY);
        }

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
    }

    public void launchUpdateService() {
        Intent intent = new Intent(this, StepCountUpdateService.class);
        startService(intent);
    }

    public void gotoNewRun() {
        Intent intent = new Intent(this, CurrentWalkActivity.class);
        startActivity(intent);
    }

    public void gotoMock() {
        Intent intent = new Intent(this, MockActivity.class);
        startActivity(intent);
    }

    public void gotoRoutes() {
        Intent intent = new Intent(this, RoutesActivity.class);
        finish();
        startActivity(intent);
    }

    public void showHeightDialog() {
        // Create an instance of the dialog fragment and show it
        HeightPromptDialog heightPrompt = new HeightPromptDialog();

        SharedPreferences savedHeightPref = getSharedPreferences("saved_height", MODE_PRIVATE);
        float savedHeight = savedHeightPref.getFloat("user_height", -1);
        boolean useHeight = savedHeightPref.getBoolean("use_height", true);

        if (savedHeight <= 0 && useHeight) {
            heightPrompt.show(getSupportFragmentManager(), "Height");
        }
    }

    public void setStepCount(int stepCount) {
        WalkWalkRevolutionApplication.stepCount.set(stepCount);
        textSteps.setText(String.valueOf(stepCount));
    }

    public void updateStepCount() {
        fitnessService.updateStepCount();
    }
}
