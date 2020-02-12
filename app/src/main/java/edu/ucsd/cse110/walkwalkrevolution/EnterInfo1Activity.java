package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EnterInfo1Activity extends AppCompatActivity{

    //drop down features selection
    private Spinner sp_trackLoop;
    private static final String[] loop = {"loop", "out and back"};

    private Spinner sp_trackFlat;
    private static final String[] flat = {"flat", "hilly"};

    private Spinner sp_trackStreets;
    private static final String[] streets = {"streets", "trail"};

    private Spinner sp_trackEven;
    private static final String[] even = {"even surface", "uneven surface"};

    private Spinner sp_trackDifficulty;
    private static final String[] difficulty = {"moderate", "easy", "difficult"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_info1);


        // create new route
        Route newRoute;

        // drop down features selection
        sp_trackLoop = (Spinner)findViewById(R.id.sp_trackLoop);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,loop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackLoop.setAdapter(adapter);
        sp_trackLoop.setSelection(0); //set default selection to 0
        sp_trackLoop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = loop[position];
                Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_trackFlat = (Spinner)findViewById(R.id.sp_trackFlat);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,flat);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackFlat.setAdapter(adapter2);
        sp_trackFlat.setSelection(0); //set default selection to 0
        sp_trackFlat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = flat[position];
                Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_trackStreets = (Spinner)findViewById(R.id.sp_trackStreets);
        ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,streets);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackStreets.setAdapter(adapter3);
        sp_trackStreets.setSelection(0); //set default selection to 0
        sp_trackStreets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = streets[position];
                Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_trackEven = (Spinner)findViewById(R.id.sp_trackEven);
        ArrayAdapter<String>adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,even);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackEven.setAdapter(adapter4);
        sp_trackEven.setSelection(0); //set default selection to 0
        sp_trackEven.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = even[position];
                Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_trackDifficulty = (Spinner)findViewById(R.id.sp_trackDifficulty);
        ArrayAdapter<String>adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,difficulty);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_trackDifficulty.setAdapter(adapter5);
        sp_trackDifficulty.setSelection(0); //set default selection to 0
        sp_trackDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valueSelected = difficulty[position];
                Toast.makeText(EnterInfo1Activity.this, valueSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button bt_enterInfo2 = (Button) findViewById(R.id.bt_gotoEnterInfo2);

        // check if user pressed next
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
