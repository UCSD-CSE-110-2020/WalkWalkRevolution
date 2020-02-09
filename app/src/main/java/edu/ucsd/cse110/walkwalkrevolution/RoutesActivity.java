package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        
    }

    public void gotoMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void gotoNewRoute() {
        Intent intent = new Intent(this, NewRouteActivity.class);
        startActivity(intent);
    }
}
