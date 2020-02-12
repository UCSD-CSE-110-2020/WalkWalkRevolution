package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
                save();
                gotoRoute();
            }
        });
    }

    public void gotoRoute() {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

    private void save() {
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();

        EditText notes = (EditText) findViewById(R.id.notesText);
        tempRouteEdit.putString("notes", notes.getText().toString());

        CheckBox favorite = (CheckBox) findViewById(R.id.markFavorite);

        if(favorite.isChecked())
            tempRouteEdit.putString("isFavorite", "true");
        else
            tempRouteEdit.putString("isFavorite", "false");

        tempRouteEdit.apply();
    }
}
