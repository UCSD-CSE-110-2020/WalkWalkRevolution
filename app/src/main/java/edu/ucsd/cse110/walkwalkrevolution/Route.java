package edu.ucsd.cse110.walkwalkrevolution;

import java.io.Serializable;
import java.util.Calendar;

// store a single route
public class Route implements Serializable {

    // Necessary Features
    private String name;
    private String startingPoint;

    // Distance info / Stats
    private int steps;
    // distance in miles
    private float distance;
    private Calendar lastRun;

    // Additional Features
    private String features[];
    private String notes;
    private boolean isFavorite;

    /**
     * Constructors
     */
    Route() {
        name = "ERROR NAME";
        startingPoint = "ERROR STARTING POINT";
        steps = 0;
        distance = 0;
        lastRun = null;
        notes = "error";
        features = new String[5];
        isFavorite = false;
    }

    Route(String name, String startingPoint) {
        this.name = name;
        this.startingPoint = startingPoint;
        steps = 0;
        distance = 0;
        lastRun = null;
        notes = "";
        features = new String[5];
        isFavorite = false;
    }

    Route(String name, String startingPoint, int steps, float distance,
          Calendar lastRun, String notes, String style,
          String terrain, String enviroment, String surface,
          String difficulty, boolean favorite) {
        this.name = name;
        this.startingPoint = startingPoint;
        this.steps = steps;
        this.distance = distance;
        this.lastRun = lastRun;
        this.notes = notes;
        features = new String[]{style, terrain, enviroment, surface, difficulty};
        isFavorite = favorite;
    }

    /**
     * Get / Set Methods for necessary features
     */
    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void setStartingPoint(String newStartingPoint) {
        startingPoint = newStartingPoint;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    /**
     * Get / Set Methods For Statistics
     */
    public void setSteps(int newSteps) {
        steps = newSteps;
    }

    public int getSteps() {
        return steps;
    }

    public void setDistance(float newDistance) {
        distance = newDistance;
    }

    public float getDistance() {
        return distance;
    }

    public void setLastRun(Calendar newLastRun) {
        lastRun = newLastRun;
    }

    public Calendar getLastRun() {
        return lastRun;
    }

    /**
     * Get / Set Methods For Other Features
     */

    public void setFeatures(String newPath, String newTerrain,
                            String newEnviroment, String newSurface,
                            String newDifficulty) {
        features[0] = newPath;
        features[1] = newTerrain;
        features[2] = newEnviroment;
        features[3] = newSurface;
        features[4] = newDifficulty;
    }

    public String[] getFeatures() {
        return features;
    }

    public void setNotes(String newNotes) {
        notes = newNotes;
    }

    public String getNotes() {
        return notes;
    }

    public void setFavorite(boolean bool) {
        isFavorite = bool;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

}
