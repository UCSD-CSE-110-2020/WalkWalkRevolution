package edu.ucsd.cse110.walkwalkrevolution;

public abstract class MeasurementConverter {

    public static final float INCHES_IN_FOOT = 12.0f;
    public static final float FEET_IN_MILE = 5280.00f;

    public static final float STEP_TO_DISTANCE = 0.414f; // Multiplier for distance calculation

    public static float stepToMiles(int stepCount, float heightInInches) {
        return ((((float)stepCount) * (heightInInches / INCHES_IN_FOOT)) * STEP_TO_DISTANCE) / FEET_IN_MILE;
    }
}
