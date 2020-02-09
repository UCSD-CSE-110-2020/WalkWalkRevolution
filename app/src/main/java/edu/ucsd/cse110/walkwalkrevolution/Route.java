package edu.ucsd.cse110.walkwalkrevolution;

// store a single route
public class Route {

    private long steps;
    // distance in miles
    private double distance;
    private String name;
    private String startingPoint;
    private String notes;
    private boolean isFavorite;

    Route() {
        steps = 6666;
        distance = 1.23;
        name = "error name";
        startingPoint = "error starting point";
        notes = "error notes";
        isFavorite = false;
    }

    public void setName(String str) {
        name = str;
    }

    public void startingPoint(String str) {
        startingPoint = str;
    }

    public void notes(String str) {
        notes = str;
    }

    public void isFavorite(boolean bool) {
        isFavorite = bool;
    }

}
