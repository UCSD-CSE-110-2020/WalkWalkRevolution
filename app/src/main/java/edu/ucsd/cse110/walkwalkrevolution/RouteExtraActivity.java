package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class RouteExtraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_extra);

        Button bt_done = (Button) findViewById(R.id.bt_done);

        // check if user pressed done
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                gotoRoutes();
            }
        });
    }

    public void gotoRoutes() {
        Intent intent = new Intent(this, RoutesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void save() {
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();

        EditText notes = (EditText) findViewById(R.id.box_notesText);
        tempRouteEdit.putString("notes", notes.getText().toString());

        CheckBox favorite = (CheckBox) findViewById(R.id.option_favorite);

        if(favorite.isChecked())
            tempRouteEdit.putString("isFavorite", "true");
        else
            tempRouteEdit.putString("isFavorite", "false");

        tempRouteEdit.apply();

        // logging for current route data

        Log.d("route","name " + tempRoute.getString("name", ""));
        Log.d("route","startingPoint " + tempRoute.getString("startingPoint", ""));
        Log.d("route","steps " + tempRoute.getString("steps", ""));
        Log.d("route","distance " + tempRoute.getString("distance", ""));
        Log.d("route","notes " + tempRoute.getString("notes", ""));
        Log.d("route","isFavorite " + tempRoute.getString("isFavorite", ""));
        Log.d("route","style " + tempRoute.getString("style", ""));
        Log.d("route","terrain " + tempRoute.getString("terrain", ""));
        Log.d("route","environment " + tempRoute.getString("environment", ""));
        Log.d("route","surface " + tempRoute.getString("surface", ""));
        Log.d("route","difficulty "  + tempRoute.getString("difficulty", ""));

        // create new route

        Route newRoute = new Route();
        newRoute.setName(tempRoute.getString("name", ""));
        newRoute.setStartingPoint(tempRoute.getString("startingPoint", ""));
        int numSteps = Integer.parseInt(tempRoute.getString("steps", "0"));
        newRoute.setSteps(numSteps);
        float distance = Float.parseFloat(tempRoute.getString("distance", "0"));
        newRoute.setDistance(distance);
        newRoute.setNotes(tempRoute.getString("notes", ""));
        newRoute.setFavorite(favorite.isChecked());
        newRoute.setFeatures(tempRoute.getString("style", ""),tempRoute.getString("terrain", ""),
                tempRoute.getString("environment", ""), tempRoute.getString("surface", ""), tempRoute.getString("difficulty", ""));

        // save new route
        RoutesManager routesManager = new RoutesManager(this);
        routesManager.addRoute(newRoute);
    }
}