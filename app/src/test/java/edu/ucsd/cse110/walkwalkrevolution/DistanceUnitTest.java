package edu.ucsd.cse110.walkwalkrevolution;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class DistanceUnitTest {

    @Test
    public void testZeroStepsDistance() {
        int stepCount = 0;
        int heightInInches = 67;
        assertEquals(0.0f, MeasurementConverter.stepToMiles(stepCount, heightInInches));
    }

    @Test
    public void testSmallStepsDistance() {
        int stepCount = 100;
        int heightInInches = 67;
        assertEquals(0.0438f, MeasurementConverter.stepToMiles(stepCount, heightInInches), 0.01f);
    }

    @Test
    public void testLargeStepsDistance() {
        int stepCount = 10000;
        int heightInInches = 67;
        assertEquals(4.378f, MeasurementConverter.stepToMiles(stepCount, heightInInches), 0.01f);
    }

    @Test
    public void testShortHeightStepsDistance() {
        int stepCount = 1000;
        int heightInInches = 50;
        assertEquals(0.327f, MeasurementConverter.stepToMiles(stepCount, heightInInches), 0.01f);
    }

    @Test
    public void testTallHeightStepsDistance() {
        int stepCount = 1000;
        int heightInInches = 80;
        assertEquals(0.523f, MeasurementConverter.stepToMiles(stepCount, heightInInches), 0.01f);
    }
}
