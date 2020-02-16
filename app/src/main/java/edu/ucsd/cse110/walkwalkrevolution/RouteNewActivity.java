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
import android.widget.Toast;

public class RouteNewActivity extends AppCompatActivity{

    //drop down features selection
    private Spinner sp_routeStyle;
    private static final String[] style = {"Style:", "Loop", "Out and Back"};

    private Spinner sp_routeTerrain;
    private static final String[] terrain = {"Terrain:", "Flat", "Hilly"};

    private Spinner sp_routeEnvironment;
    private static final String[] environment = {"Environment:", "Streets", "Trail"};

    private Spinner sp_routeSurface;
    private static final String[] surface = {"Surface:", "Even Surface", "Uneven Surface"};

    private Spinner sp_routeDifficulty;
    private static final String[] difficulty = {"Difficulty:", "Easy", "Moderate", "Difficult"};

    Walk prevWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_new);

        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();

        Intent i = getIntent();
        prevWalk = (Walk)i.getSerializableExtra("finalWalk");

        // add a new walk
        if (prevWalk == null) {
            prevWalk = new Walk("0",0,0);
        }

        // drop down features selection
        sp_routeStyle = (Spinner) findViewById(R.id.sp_routeStyle);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,style);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_routeStyle.setAdapter(adapter);
        sp_routeStyle.setSelection(0); //set default selection to 0
        sp_routeStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = "";
                if (position != 0) {
                    valueSelected = style[position];
                }
                //Toast.makeText(RouteNewActivity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("style", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("style", "");
                tempRouteEdit.apply();
            }
        });


        sp_routeTerrain = (Spinner) findViewById(R.id.sp_routeTerrain);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,terrain);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_routeTerrain.setAdapter(adapter2);
        sp_routeTerrain.setSelection(0); //set default selection to 0
        sp_routeTerrain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = "";
                if (position != 0) {
                    valueSelected = terrain[position];
                }
                //Toast.makeText(RouteNewActivity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("terrain", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("terrain", "");
                tempRouteEdit.apply();
            }
        });


        sp_routeEnvironment = (Spinner) findViewById(R.id.sp_routeEnvironment);
        ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,environment);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_routeEnvironment.setAdapter(adapter3);
        sp_routeEnvironment.setSelection(0); //set default selection to 0
        sp_routeEnvironment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = "";
                if (position != 0) {
                    valueSelected = environment[position];
                }
                //Toast.makeText(RouteNewActivity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("environment", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("environment", "");
                tempRouteEdit.apply();
            }
        });


        sp_routeSurface = (Spinner) findViewById(R.id.sp_routeSurface);
        ArrayAdapter<String>adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,surface);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_routeSurface.setAdapter(adapter4);
        sp_routeSurface.setSelection(0); //set default selection to 0
        sp_routeSurface.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = "";
                if (position != 0) {
                    valueSelected = surface[position];
                }
                //Toast.makeText(RouteNewActivity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("surface", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("surface", "");
                tempRouteEdit.apply();
            }
        });

        sp_routeDifficulty = (Spinner) findViewById(R.id.sp_routeDifficulty);
        ArrayAdapter<String>adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,difficulty);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_routeDifficulty.setAdapter(adapter5);
        sp_routeDifficulty.setSelection(0); //set default selection to 0
        sp_routeDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = "";
                if (position != 0) {
                    valueSelected = difficulty[position];
                }
                //Toast.makeText(RouteNewActivity.this, valueSelected, Toast.LENGTH_SHORT).show();
                tempRouteEdit.putString("difficulty", valueSelected);
                tempRouteEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tempRouteEdit.putString("difficulty", "");
                tempRouteEdit.apply();
            }
        });

        Button bt_next = (Button) findViewById(R.id.bt_next);

        // check if user pressed next
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Title must be entered
                if (((EditText) findViewById(R.id.box_title)).getText().toString().matches("")) {
                    Toast.makeText(RouteNewActivity.this, "Please Enter a Title", Toast.LENGTH_SHORT).show();
                }
                else {
                    save();
                    gotoRouteExtra();
                }
            }
        });
    }

    public void save() {
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        SharedPreferences.Editor tempRouteEdit = tempRoute.edit();

        // get route name / starting point
        EditText eName = (EditText) findViewById(R.id.box_title);
        EditText eStartingPoint = (EditText) findViewById(R.id.box_startingPoint);
        tempRouteEdit.putString("steps", Integer.toString(prevWalk.getSteps()));
        tempRouteEdit.putString("distance", Double.toString(prevWalk.getDistance()));
        tempRouteEdit.putString("name", eName.getText().toString());
        tempRouteEdit.putString("startingPoint", eStartingPoint.getText().toString());
        tempRouteEdit.apply();
    }

    public void gotoRouteExtra() {
        Intent intent = new Intent(this, RouteExtraActivity.class);
        startActivity(intent);
    }
}
