package edu.ucsd.cse110.walkwalkrevolution;
import java.io.Serializable;

public class Walk implements Serializable {
    private String totalTime;
    private int steps;
    private float distance;

    public Walk(String t, int st, float dis) {
        totalTime = t;
        steps = st;
        distance = dis;
    }

    public void setTotalTime(String t) {
        totalTime = t;
    }

    public void setSteps(int st) {
        steps = st;
    }

    public void setDistance(float dis) {
        distance = dis;
    }

    public String getTotalTime() {
        return this.totalTime;
    }

    public int getSteps() {
        return this.steps;
    }

    public float getDistance() {
        return this.distance;
    }


}
