package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.ucsd.cse110.walkwalkrevolution.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void gotoNewRun() {
        Intent intent = new Intent(this, CurrentRunActivity.class);
        startActivity(intent);
    }

    public void gotoRoutes() {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }
}
