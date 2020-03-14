package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProposeWalkActivity extends AppCompatActivity {
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    int day, month, year, hour, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_walk);

        // Check if user pressed send button
        Button bt_send = (Button) findViewById(R.id.bt_sendProposeWalk);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // day and time must be entered
                if (((EditText) findViewById(R.id.box_date)).getText().toString().matches("")) {
                    Toast.makeText(ProposeWalkActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                }
                else if (((EditText) findViewById(R.id.box_time)).getText().toString().matches("")) {
                    Toast.makeText(ProposeWalkActivity.this, "Please enter a time", Toast.LENGTH_SHORT).show();
                }
                else {
                    save();
                    gotoMainMenu();
                }
            }
        });

        EditText box_date = (EditText) findViewById(R.id.box_date);

        box_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(ProposeWalkActivity.this,
                        R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                box_date.setText((month + 1) + "/" + day + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        EditText box_time = (EditText) findViewById(R.id.box_time);

        box_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minutes = calendar.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(ProposeWalkActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                box_time.setText((hour == 12 ? "12" : hour % 12) + ":" + minutes + " " + (hour / 12 == 1 ? "PM" : "AM"));
                            }
                        }, hour, minutes, false);
                timePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePicker.show();
            }
        });
    }


    public void save() {


    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        startActivity(intentMainMenu);
    }
}
