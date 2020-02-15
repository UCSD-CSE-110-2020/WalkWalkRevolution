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
import java.util.Calendar;
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
        savedRoutesInfo = context.getSharedPreferences(
                context.getResources().getString(R.string.routeInfo_store), MODE_PRIVATE);
        savedRoutesFeatures = context.getSharedPreferences(
                context.getResources().getString(R.string.routeFeature_store), MODE_PRIVATE);
        infoRouteEditor = savedRoutesInfo.edit();
        featureRouteEditor = savedRoutesFeatures.edit();
    }

    // load saved routes
    public ArrayList<Route> loadAll() {
        Map<String, ?> routeMap = new TreeMap<>(savedRoutesInfo.getAll());
        ArrayList<Route> loadList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : routeMap.entrySet()) {
            loadList.add(loadRoute(entry));
        }
        return loadList;
    }

    public Route loadRoute(Map.Entry<String, ?> route) {
        Calendar lastRun = null;

        int year = savedRoutesFeatures.getInt(
                route.getKey() + R.string.year_tag, -1);
        int month = savedRoutesFeatures.getInt(
                route.getKey() + R.string.month_tag, -1);
        int day = savedRoutesFeatures.getInt(
                route.getKey() + R.string.day_tag, -1);
        if (year > 0 && month > 0 && day > 0) {
            lastRun.set(year, month, day);
        }

        Route loadedRoute = new Route(
                route.getKey(),
                route.getValue().toString(),
                savedRoutesFeatures.getInt(
                        route.getKey() + R.string.steps_tag, 0),
                savedRoutesFeatures.getFloat(
                        route.getKey() + R.string.distance_tag, 0),
                lastRun,
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.notes_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.path_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.terrain_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.environment_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.surface_tag, ""),
                savedRoutesFeatures.getString(
                        route.getKey() + R.string.difficulty_tag, ""),
                savedRoutesFeatures.getBoolean(
                        route.getKey() + R.string.favorite_tag, false)
        );
        return loadedRoute;
    }

    // add a new route
    public void addRoute(Route newRoute) {
        String startingLocation = newRoute.getStartingPoint();
        infoRouteEditor.putString(newRoute.getName(), startingLocation);

        int steps = newRoute.getSteps();
        featureRouteEditor.putInt(newRoute.getName() + R.string.steps_tag, steps);
        float distance = newRoute.getSteps();
        featureRouteEditor.putFloat(newRoute.getName() + R.string.distance_tag, distance);

        int year = -1;
        int month = -1;
        int day = -1;

        if (newRoute.getLastRun() != null) {
            year = newRoute.getLastRun().get(Calendar.YEAR);
            month = newRoute.getLastRun().get(Calendar.MONTH);
            day = newRoute.getLastRun().get(Calendar.DAY_OF_MONTH);
        }

        featureRouteEditor.putInt(newRoute.getName() + R.string.year_tag, year);
        featureRouteEditor.putInt(newRoute.getName() + R.string.month_tag, month);
        featureRouteEditor.putInt(newRoute.getName() + R.string.day_tag, day);

        String notes = newRoute.getNotes();
        featureRouteEditor.putString(newRoute.getName() + R.string.notes_tag, notes);

        String path = newRoute.getFeatures()[0];
        featureRouteEditor.putString(newRoute.getName() + R.string.path_tag, path);
        String terrain = newRoute.getFeatures()[1];
        featureRouteEditor.putString(newRoute.getName() + R.string.terrain_tag, terrain);
        String enviroment = newRoute.getFeatures()[2];
        featureRouteEditor.putString(newRoute.getName() + R.string.environment_tag, enviroment);
        String surface = newRoute.getFeatures()[3];
        featureRouteEditor.putString(newRoute.getName() + R.string.surface_tag, surface);
        String difficulty = newRoute.getFeatures()[4];
        featureRouteEditor.putString(newRoute.getName() + R.string.difficulty_tag, difficulty);

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
        featureRouteEditor.remove(name + R.string.year_tag);
        featureRouteEditor.remove(name + R.string.month_tag);
        featureRouteEditor.remove(name + R.string.day_tag);
        featureRouteEditor.remove(name + R.string.notes_tag);
        featureRouteEditor.remove(name + R.string.path_tag);
        featureRouteEditor.remove(name + R.string.terrain_tag);
        featureRouteEditor.remove(name + R.string.environment_tag);
        featureRouteEditor.remove(name + R.string.surface_tag);
        featureRouteEditor.remove(name + R.string.difficulty_tag);
        featureRouteEditor.remove(name + R.string.favorite_tag);

        infoRouteEditor.apply();
        featureRouteEditor.apply();
    }

    // delete all routes
    public void clearRoutes() {
        infoRouteEditor.clear();
        featureRouteEditor.clear();
        infoRouteEditor.apply();
        featureRouteEditor.apply();
    }
}
