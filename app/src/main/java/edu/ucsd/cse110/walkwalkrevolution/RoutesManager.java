package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


// store all routes and load routes from user preferences
public class RoutesManager {

    private Context context;
    private ArrayList<Route> routes;
    SharedPreferences savedRoutes;
    SharedPreferences.Editor routeEditor;

    RoutesManager(Context currentContext) {
        context = currentContext;
        savedRoutes = context.getSharedPreferences("routes", MODE_PRIVATE);
        routeEditor = savedRoutes.edit();
    }

    // load saved routes
    public void loadAll() {
        Map<String, ?> routeMap = savedRoutes.getAll();
        for (Map.Entry<String, ?> entry : routeMap.entrySet()) {
            loadRoute((Map.Entry<String, Set<String>>)entry);
        }
    }

    public void loadRoute(Map.Entry<String, Set<String>> route) {
        String name = route.getKey();
        Set<String> values = route.getValue();
    }

    // add a new route
    public void addRoute(Route newRoute) {
        String routeFeatures[] = {
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

        Set<String> routeSet = new HashSet<>(Arrays.asList(routeFeatures));
        routeEditor.putStringSet(newRoute.getName(), routeSet);

        routeEditor.apply();
    }

    // delete a route
    public void deleteRoute(String name) {
        routeEditor.remove(name);
        routeEditor.apply();
    }

}
