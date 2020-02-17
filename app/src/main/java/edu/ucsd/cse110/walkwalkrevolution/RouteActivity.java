package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class RouteActivity extends AppCompatActivity {

    public static final String TAG = "RouteActivity";

    Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Log.d(TAG, "Creating route activity");

        Intent intent = getIntent();
        route = (Route)intent.getSerializableExtra("route");

        // set ui
        TextView title = (TextView) findViewById(R.id.title_routeName);
        title.setText(route.getName());

        TextView startingPoint = (TextView) findViewById(R.id.box_startingPoint);
        startingPoint.setText(route.getStartingPoint());

        TextView lastWalked = (TextView) findViewById(R.id.box_lastWalked);

        Calendar lastRun = route.getLastRun();

        if (lastRun == null) {
            lastWalked.setText(R.string.never_run);
        } else {
            lastWalked.setText(lastRun.get(Calendar.MONTH) + "/" +
                    lastRun.get(Calendar.DAY_OF_MONTH) + "/" +
                    lastRun.get(Calendar.YEAR));
        }

        // display features
        TextView featureView = (TextView) findViewById(R.id.box_features);
        String[] features = route.getFeatures();
        String featuresString = "";
        for (int i = 0; i < 5; i++) {
            if (!features[i].equals("")) {
                featuresString += features[i] + "\n";
            }
        }
        featureView.setText(featuresString);

        // display notes
        TextView notes = (TextView) findViewById(R.id.box_notes);
        notes.setText(route.getNotes());

        // set isFavorite
        TextView isFavorite = (TextView) findViewById(R.id.box_isFavorite);
        if(route.getFavorite())
            isFavorite.setText("TRUE");
        else
            isFavorite.setText("FALSE");


        // Check if user pressed routes button
        Button bt_routes = (Button) findViewById(R.id.bt_routes);
        bt_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRoutes();
            }
        });

        // Check if user pressed start new run button
        Button bt_newRun = (Button) findViewById(R.id.bt_startWalk);
        bt_newRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRun();
            }
        });
    }

    public void gotoRoutes() {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

    public void gotoNewRun() {
        Intent intent = new Intent(this, CurrentWalkActivity.class);
        SharedPreferences savedHeightPref = getSharedPreferences("saved_height", MODE_PRIVATE);
        float savedHeight = savedHeightPref.getFloat("user_height", -1);
        intent.putExtra("savedHeight", savedHeight);
        intent.putExtra("title", route.getName());
        intent.putExtra("route", route);
        intent.putExtra("isSavedRoute", true);
        startActivity(intent);
    }
}
