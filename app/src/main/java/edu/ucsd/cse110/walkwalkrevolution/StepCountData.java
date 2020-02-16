package edu.ucsd.cse110.walkwalkrevolution;

public class StepCountData {

    private static int value = 0;

    public static void set(int stepCount) {
        value = stepCount;
    }

    public static int get() {
        return value;
    }
}
