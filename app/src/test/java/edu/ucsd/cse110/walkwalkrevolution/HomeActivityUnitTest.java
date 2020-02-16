package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessServiceFactory;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class HomeActivityUnitTest {
    private final String FITNESS_SERVICE_TEST_KEY = "TEST_SERVICE";

    private Context context;
    private Intent intent;
    private int nextStepCount;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();

        // Add the test fitness service to the factory
        FitnessServiceFactory.put(FITNESS_SERVICE_TEST_KEY, TestFitnessService::new);

        intent = new Intent(context, HomeActivity.class);
        intent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, FITNESS_SERVICE_TEST_KEY);
    }

    @Test
    public void testZeroUpdateSteps() {
        nextStepCount = 0;

        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            // Update the step count
            activity.updateStepCount();
            TextView box_dailySteps = activity.findViewById(R.id.box_dailySteps);
            assertThat(box_dailySteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    @Test
    public void testSmallUpdateSteps() throws TimeoutException {
        nextStepCount = 10;

        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            // Update the step count
            activity.updateStepCount();
            TextView box_dailySteps = activity.findViewById(R.id.box_dailySteps);
            assertThat(box_dailySteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    @Test
    public void testLargeUpdateSteps() {
        nextStepCount = 1337;

        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            // Update the step count
            activity.updateStepCount();
            TextView box_dailySteps = activity.findViewById(R.id.box_dailySteps);
            assertThat(box_dailySteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private HomeActivity HomeActivity;

        public TestFitnessService(HomeActivity HomeActivity) {
            this.HomeActivity = HomeActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount to " + nextStepCount);
            HomeActivity.setStepCount(nextStepCount);
        }
    }
}