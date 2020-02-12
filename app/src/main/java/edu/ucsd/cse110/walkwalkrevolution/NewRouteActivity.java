package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewRouteActivity extends AppCompatActivity {

    //drop down features selection
    private Spinner sp_trackLoop;
    private static final String[] style = {"LOOP", "OUT AND BACK"};

    private Spinner sp_trackFlat;
    private static final String[] terrain = {"FLAT", "HILLY"};

    private Spinner sp_trackStreets;
    private static final String[] environment = {"STREETS", "TRAIL"};

    private Spinner sp_trackEven;
    private static final String[] surface = {"EVEN SURFACE", "UNEVEN SURFACE"};

    private Spinner sp_trackDifficulty;
    private static final String[] difficulty = {"MODERATE", "EASY", "DIFFICULT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);




        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();

        // drop down features selection
        sp_trackLoop = (Spinner)findViewById(R.id.sp_trackLoop2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,style);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackLoop.setAdapter(adapter);
        sp_trackLoop.setSelection(0); //set default selection to 0
        sp_trackLoop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = style[position];
                //Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("style", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("style", style[0]);
                tempRouteEdit.apply();
            }
        });


        sp_trackFlat = (Spinner)findViewById(R.id.sp_trackFlat2);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,terrain);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackFlat.setAdapter(adapter2);
        sp_trackFlat.setSelection(0); //set default selection to 0
        sp_trackFlat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = terrain[position];
                //Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("terrain", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("terrain", terrain[0]);
                tempRouteEdit.apply();
            }
        });


        sp_trackStreets = (Spinner)findViewById(R.id.sp_trackStreets2);
        ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,environment);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackStreets.setAdapter(adapter3);
        sp_trackStreets.setSelection(0); //set default selection to 0
        sp_trackStreets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = environment[position];
                //Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("environment", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("environment", environment[0]);
                tempRouteEdit.apply();
            }
        });


        sp_trackEven = (Spinner)findViewById(R.id.sp_trackEven2);
        ArrayAdapter<String>adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,surface);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackEven.setAdapter(adapter4);
        sp_trackEven.setSelection(0); //set default selection to 0
        sp_trackEven.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = surface[position];
                //Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("surface", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("surface", surface[0]);
                tempRouteEdit.apply();
            }
        });

        sp_trackDifficulty = (Spinner)findViewById(R.id.sp_trackDifficulty2);
        ArrayAdapter<String>adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,difficulty);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackDifficulty.setAdapter(adapter5);
        sp_trackDifficulty.setSelection(0); //set default selection to 0
        sp_trackDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = difficulty[position];
                //Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("difficulty", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("difficulty", difficulty[0]);
                tempRouteEdit.apply();
            }
        });



        Button bt_enterInfo2 = (Button) findViewById(R.id.bt_newRoutegotoEnterInfo2);

        // check if user pressed next
        bt_enterInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                gotoEnterInfo2();

            }
        });



    }


    public void save() {
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();
        // get route name / starting point
        EditText eName = (EditText) findViewById(R.id.enterName2);
        EditText eStartingPoint = (EditText) findViewById(R.id.enterStartingPoint2);
        //EditText textSteps = (EditText) findViewById(R.id.textSteps);
        tempRouteEdit.putString("steps", "0");
        tempRouteEdit.putString("distance", "0");
        tempRouteEdit.putString("name", eName.getText().toString());
        tempRouteEdit.putString("startingPoint", eStartingPoint.getText().toString());
        tempRouteEdit.apply();
    }

    public void gotoEnterInfo2() {
        Intent intent = new Intent(this, EnterInfo2Activity.class);
        startActivity(intent);
    }
}

