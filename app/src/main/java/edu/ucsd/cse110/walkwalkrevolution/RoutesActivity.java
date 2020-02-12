package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Set;

public class RoutesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        Button bt_mainMenu = (Button) findViewById(R.id.bt_gotoMainMenu);

        
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();
        Log.d("myTag","name " + tempRoute.getString("name", ""));
        Log.d("myTag","startingPoint " + tempRoute.getString("startingPoint", ""));
        Log.d("myTag","steps " + tempRoute.getString("steps", ""));
        Log.d("myTag","distance " + tempRoute.getString("distance", ""));
        Log.d("myTag","notes " + tempRoute.getString("notes", ""));
        Log.d("myTag","isFavorite " + tempRoute.getString("isFavorite", ""));
        Log.d("myTag","style " + tempRoute.getString("style", ""));
        Log.d("myTag","terrain " + tempRoute.getString("terrain", ""));
        Log.d("myTag","enviroment " + tempRoute.getString("environment", ""));
        Log.d("myTag","surface " + tempRoute.getString("surface", ""));
        Log.d("myTag","difficulty "  + tempRoute.getString("difficulty", ""));


        // check if user pressed main menu
        bt_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainMenu();
            }
        });

        Button bt_newRoute = (Button) findViewById(R.id.bt_gotoNewRoute);

        // check if user pressed main menu
        bt_newRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRoute();
            }
        });

        // Setup RouteManager
        RoutesManager manager = new RoutesManager(this);
        // Load Route List
        loadRouteList(manager.loadAll());
    }

    public void loadRouteList(ArrayList<String[]> loadList) {
        // Async Task? I think so
        for (String[] routeValue : loadList) {

        }
    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, MainActivity.class);
        startActivity(intentMainMenu);
    }

    public void gotoNewRoute() {
        Intent intentNewRoute = new Intent(this, NewRouteActivity.class);
        startActivity(intentNewRoute);
    }
}
