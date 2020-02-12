package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
