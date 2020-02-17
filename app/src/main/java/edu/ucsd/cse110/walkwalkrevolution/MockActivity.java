package edu.ucsd.cse110.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MockActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION = "edu.ucsd.cse110.STEP_COUNT_MOCK";
    public static final int MOCK_STEP_INCREMENT = 500;

    private static final String TAG = "MockActivity";

    private int mockStepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        Button bt_addSteps = findViewById(R.id.bt_addSteps);
        bt_addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mockStepCount += MOCK_STEP_INCREMENT;
                Toast.makeText(MockActivity.this, "Mocked step count fixed at " + mockStepCount, Toast.LENGTH_SHORT).show();
                addMockSteps();
            }
        });

        EditText box_timeMock = findViewById(R.id.box_timeMock);

        Button bt_submit = findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long timeMock = Long.parseLong(box_timeMock.getText().toString());
                    Toast.makeText(MockActivity.this, "Mocked time fixed at " + timeMock, Toast.LENGTH_SHORT).show();
                    setMockTime(timeMock);
                } catch (NumberFormatException e) {
                    Toast.makeText(MockActivity.this, "Please Enter a Valid Time", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private void addMockSteps() {
        Log.d(TAG, "Adding " + mockStepCount + " steps");
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("Mock Step Count", mockStepCount);
        sendBroadcast(intent);
    }

    private void setMockTime(long timeInMilliseconds) {
        Log.d(TAG, "Setting mock time to " + timeInMilliseconds + " milliseconds");
        CurrentWalkActivity.setClock(new SerializableFixedClock(timeInMilliseconds));
    }
}
