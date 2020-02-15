package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.walkwalkrevolution.fitness.FitnessServiceFactory;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class HomeActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeActivity.class);
        intent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testZeroUpdateSteps() {
        nextStepCount = 0;

        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textSteps = activity.findViewById(R.id.textSteps);
            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    @Test
    public void testSmallUpdateSteps() {
        nextStepCount = 10;

        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textSteps = activity.findViewById(R.id.textSteps);
            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    @Test
    public void testLargeUpdateSteps() {
        nextStepCount = 1337;

        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textSteps = activity.findViewById(R.id.textSteps);
            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
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