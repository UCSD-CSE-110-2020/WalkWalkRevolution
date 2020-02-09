package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);

        Button bt_enterInfo2 = (Button) findViewById(R.id.bt_newRoutegotoEnterInfo2);

        // check if user pressed next button
        bt_enterInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEnterInfo2();
            }
        });
    }

    public void gotoEnterInfo2() {
        Intent intent = new Intent(this, EnterInfo2Activity.class);
        startActivity(intent);
    }
}
