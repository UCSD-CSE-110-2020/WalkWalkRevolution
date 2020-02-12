package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkwalkrevolution.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showHeightDialog();

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(StepCountActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });
        launchStepCountActivity();

        Button bt_newRun = (Button) findViewById(R.id.bt_startNewRun);

        // check if user pressed start new run button
        bt_newRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRun();
            }
        });

        Button bt_routes = (Button) findViewById(R.id.bt_gotoRoute2);

        // check if user pressed routes button
        bt_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRoutes();
            }
        });
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }

    public void gotoNewRun() {
        Intent intent = new Intent(this, CurrentRunActivity.class);
        startActivity(intent);
    }

    public void gotoRoutes() {
        Intent intent = new Intent(this, RoutesActivity.class);
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
}
