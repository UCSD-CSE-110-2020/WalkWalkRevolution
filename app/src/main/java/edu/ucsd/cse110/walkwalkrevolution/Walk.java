package edu.ucsd.cse110.walkwalkrevolution;

public class Walk {
    private String totalTime;
    private int speed;
    private int steps;
    private double distance;

    public Walk(String t, int s, int st, double dis) {
        totalTime = t;
        speed = s;
        steps = st;
        distance = dis;
    }

    public void setTotalTime(String t) {
        totalTime = t;
    }

    public void setSpeed(int s) {
        speed = s;
    }

    public void setSteps(int st) {
        steps = st;
    }

    public void setDistance(double dis) {
        distance = dis;
    }

    public String getTotalTime() {
        return this.totalTime;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getSteps() {
        return this.steps;
    }

    public double getDistance() {
        return this.distance;
    }


}
