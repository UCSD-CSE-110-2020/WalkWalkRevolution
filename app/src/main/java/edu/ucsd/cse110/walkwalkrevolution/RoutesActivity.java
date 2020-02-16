package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        Button bt_mainMenu = (Button) findViewById(R.id.bt_home);

        // logging for current route data
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        Log.d("route","name " + tempRoute.getString("name", ""));
        Log.d("route","startingPoint " + tempRoute.getString("startingPoint", ""));
        Log.d("route","steps " + tempRoute.getString("steps", ""));
        Log.d("route","distance " + tempRoute.getString("distance", ""));
        Log.d("route","notes " + tempRoute.getString("notes", ""));
        Log.d("route","isFavorite " + tempRoute.getString("isFavorite", ""));
        Log.d("route","style " + tempRoute.getString("style", ""));
        Log.d("route","terrain " + tempRoute.getString("terrain", ""));
        Log.d("route","enviroment " + tempRoute.getString("environment", ""));
        Log.d("route","surface " + tempRoute.getString("surface", ""));
        Log.d("route","difficulty "  + tempRoute.getString("difficulty", ""));


        // check if user pressed main menu
        bt_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainMenu();
            }
        });

        FloatingActionButton bt_newRoute = (FloatingActionButton) findViewById(R.id.bt_add);

        // check if user pressed main menu
        bt_newRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRoute();
            }
        });

        // Setup RouteManager
        RoutesManager manager = new RoutesManager(this);

        // Route Testing (Alphebetical Order)
        //manager.clearRoutes();
        /* Manager Test
        manager.addRoute(new Route());
        manager.addRoute(new Route("kms", "londong"));
        manager.addRoute(new Route("wt00000f", "ok"));
        manager.addRoute(new Route("zebra", "ok"));
        manager.addRoute(new Route("aaaa", "ok"));
        manager.addRoute(new Route("adsad", "londong"));
        manager.addRoute(new Route("gadgdsg", "ok"));
        manager.addRoute(new Route("zczc", "ok"));
        manager.addRoute(new Route("aaadfdaaa", "ok"));
        */

        // Load Route List
        ListView list = (ListView) findViewById(R.id.routeList);

        ArrayList<Route> routeList = manager.loadAll();

        CustomListAdapter customAdapter = new CustomListAdapter(this, routeList);

        list.setAdapter(customAdapter);

        // Setup Onclick
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                gotoRoute(routeList.get(position));
            }
        });

    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intentMainMenu);
    }

    public void gotoNewRoute() {
        Intent intentNewRoute = new Intent(this, RouteNewActivity.class);
        startActivity(intentNewRoute);
    }

    // see more info about a specific route
    public void gotoRoute(Route selectedRoute) {
        Intent intentRoute = new Intent(this, RouteActivity.class);
        intentRoute.putExtra("route", selectedRoute);
        startActivity(intentRoute);
    }
}
