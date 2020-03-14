package edu.ucsd.cse110.walkwalkrevolution;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import static org.junit.Assert.*;

public class RouteUnitTest {

    String name;
    String startingPoint;
    int steps;
    float distance;
    Calendar calendar;
    String notes;
    String style;
    String terrain;
    String enviroment;
    String surface;
    String difficulty;
    boolean favorite;
    Route route;
    ArrayList<String> features;

    @Before
    public void setUp() throws Exception {
        name = "ucsd walk";
        startingPoint = "ebu3b";
        steps = 12345;
        distance = 12.57f;
        calendar = null;
        notes = "asdfasdfaasdf";
        style = "style";
        terrain = "ttt";
        enviroment = "env";
        surface = "surrrrrr";
        difficulty = "difff";
        favorite = false;

        route = new Route(name, startingPoint, steps, distance,
                        calendar, notes, style, terrain, enviroment, surface,
                         difficulty, favorite);

        features = route.getFeatures();
    }

    @Test
    public void testStats() {
        assertEquals(route.getName(), name);
        assertEquals(route.getStartingPoint(), startingPoint);
        assertEquals(route.getSteps(), steps);
        assertEquals(route.getDistance(), distance, 0.001);
        assertEquals(route.getLastRun(), null);
        assertEquals(route.getNotes(), notes);
        assertEquals(features.get(0), style);
        assertEquals(features.get(1), terrain);
        assertEquals(features.get(2), enviroment);
        assertEquals(features.get(3), surface);
        assertEquals(features.get(4), difficulty);
        assertEquals(route.getFavorite(), favorite);
    }


}