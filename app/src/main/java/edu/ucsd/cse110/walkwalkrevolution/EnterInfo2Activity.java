package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterInfo2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_info2);

        Button bt_route = (Button) findViewById(R.id.bt_gotoRoute);

        // check if user pressed done
        bt_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRoute();
            }
        });
    }

    public void gotoRoute() {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }
}