package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
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
    public ArrayList<String[]> loadAll() {
        Map<String, ?> routeMap = savedRoutes.getAll();
        ArrayList<String[]> loadList = new ArrayList<String[]>();
        for (Map.Entry<String, ?> entry : routeMap.entrySet()) {
            loadList.add(loadRoute((Map.Entry<String, Set<String>>)entry));
        }
        return loadList;
    }

    public String[] loadRoute(Map.Entry<String, Set<String>> route) {
        String name = route.getKey();
        String[] routeValues = (String[]) route.getValue().toArray();
        String routeData[] = {name, routeValues[1], routeValues[2]};
        return routeData;
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
