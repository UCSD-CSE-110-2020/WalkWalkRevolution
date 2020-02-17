package edu.ucsd.cse110.walkwalkrevolution;

public abstract class MeasurementConverter {

    public static final int INCHES_IN_FOOT = 12;
    public static final int FEET_IN_MILE = 5280;
    public static final int MILLIS_IN_SEC = 1000;
    public static final int SECS_IN_MIN = 60;

    public static final float STEP_TO_DISTANCE = 0.414f; // Multiplier for distance calculation

    public static float stepToMiles(int stepCount, float heightInInches) {
        return ((((float)stepCount) * (heightInInches / INCHES_IN_FOOT)) * STEP_TO_DISTANCE) / FEET_IN_MILE;
    }
}
