package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;


// store all routes and load routes from user preferences
public class RoutesManager {

    private Context context;
    private ArrayList<Route> routes;
    SharedPreferences savedRoutesInfo;
    SharedPreferences savedRoutesFeatures;
    SharedPreferences.Editor infoRouteEditor;
    SharedPreferences.Editor featureRouteEditor;

    RoutesManager(Context currentContext) {
        context = currentContext;
        savedRoutesInfo = context.getSharedPreferences("routes_info", MODE_PRIVATE);
        savedRoutesFeatures = context.getSharedPreferences("routes_features", MODE_PRIVATE);
        infoRouteEditor = savedRoutesInfo.edit();
        featureRouteEditor = savedRoutesFeatures.edit();
    }

    // load saved routes
    public ArrayList<String[]> loadAll() {
        Map<String, ?> routeMap = new TreeMap<>(savedRoutesInfo.getAll());
        ArrayList<String[]> loadList = new ArrayList<String[]>();
        for (Map.Entry<String, ?> entry : routeMap.entrySet()) {
            loadList.add(loadRoute(entry));
        }
        return loadList;
    }

    public String[] loadRoute(Map.Entry<String, ?> route) {
        String[] routeValues = {
                route.getKey(),
                route.getValue().toString(),
                Integer.toString(savedRoutesFeatures.getInt(
                        route.getKey() + R.string.steps_tag, 0)),
                Float.toString(savedRoutesFeatures.getFloat(
                        route.getKey() + R.string.distance_tag, 0)),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.path_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.terrain_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.enviroment_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.surface_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.difficulty_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.notes_tag, ""),
                Boolean.toString(savedRoutesFeatures.getBoolean(
                        route.getKey() + R.string.favorite_tag, false))
        };
        return routeValues;
    }

    // add a new route
    public void addRoute(Route newRoute) {
        String routeFeatures[] = {
                newRoute.getName(),
                newRoute.getStartingPoint(),
                Integer.toString(newRoute.getSteps()),
                Float.toString(newRoute.getDistance()),
                newRoute.getFeatures()[0],
                newRoute.getFeatures()[1],
                newRoute.getFeatures()[2],
                newRoute.getFeatures()[3],
                newRoute.getFeatures()[4],
                newRoute.getNotes(),
                Boolean.toString(newRoute.getFavorite()
                )};

        String startingLocation = newRoute.getStartingPoint();
        infoRouteEditor.putString(newRoute.getName(), startingLocation);

        int steps = newRoute.getSteps();
        featureRouteEditor.putInt(newRoute.getName() + R.string.steps_tag, steps);
        float distance = newRoute.getSteps();
        featureRouteEditor.putFloat(newRoute.getName() + R.string.distance_tag, distance);

        String path = newRoute.getFeatures()[0];
        featureRouteEditor.putString(newRoute.getName() + R.string.path_tag, path);
        String terrain = newRoute.getFeatures()[1];
        featureRouteEditor.putString(newRoute.getName() + R.string.terrain_tag, terrain);
        String enviroment = newRoute.getFeatures()[2];
        featureRouteEditor.putString(newRoute.getName() + R.string.enviroment_tag, enviroment);
        String surface = newRoute.getFeatures()[3];
        featureRouteEditor.putString(newRoute.getName() + R.string.surface_tag, surface);
        String difficulty = newRoute.getFeatures()[4];
        featureRouteEditor.putString(newRoute.getName() + R.string.difficulty_tag, difficulty);

        String notes = newRoute.getNotes();
        featureRouteEditor.putString(newRoute.getName() + R.string.notes_tag, notes);

        boolean favorite = newRoute.getFavorite();
        featureRouteEditor.putBoolean(newRoute.getName() + R.string.favorite_tag, favorite);

        infoRouteEditor.apply();
        featureRouteEditor.apply();
    }

    // delete a route
    public void deleteRoute(String name) {
        infoRouteEditor.remove(name);

        featureRouteEditor.remove(name + R.string.steps_tag);
        featureRouteEditor.remove(name + R.string.distance_tag);
        featureRouteEditor.remove(name + R.string.path_tag);
        featureRouteEditor.remove(name + R.string.terrain_tag);
        featureRouteEditor.remove(name + R.string.enviroment_tag);
        featureRouteEditor.remove(name + R.string.surface_tag);
        featureRouteEditor.remove(name + R.string.difficulty_tag);
        featureRouteEditor.remove(name + R.string.notes_tag);
        featureRouteEditor.remove(name + R.string.favorite_tag);

        infoRouteEditor.apply();
        featureRouteEditor.apply();
    }

    // delete all route
    public void clearRoutes() {
        infoRouteEditor.clear();
        featureRouteEditor.clear();
        infoRouteEditor.apply();
        featureRouteEditor.apply();
    }

}
