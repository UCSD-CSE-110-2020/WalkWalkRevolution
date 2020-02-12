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
    SharedPreferences savedRoutes;
    SharedPreferences.Editor routeEditor;

    RoutesManager(Context currentContext) {
        context = currentContext;
        savedRoutes = context.getSharedPreferences("routes", MODE_PRIVATE);
        routeEditor = savedRoutes.edit();
    }

    // load saved routes
    public ArrayList<String[]> loadAll() {
        Map<String, ?> routeMap = new TreeMap<>(savedRoutes.getAll());
        ArrayList<String[]> loadList = new ArrayList<String[]>();
        for (Map.Entry<String, ?> entry : routeMap.entrySet()) {
            loadList.add(loadRoute(entry));
        }
        return loadList;
    }

    public String[] loadRoute(Map.Entry<String, ?> route) {
        ArrayList<String> vals = new ArrayList<String>( (Set<String>)route.getValue());
        String[] routeValues = vals.toArray(new String[vals.size()]);
        Log.d("fml2",vals.get(0));
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

        Set<String> routeSet = new HashSet<>(Arrays.asList(routeFeatures));
        routeEditor.putStringSet(newRoute.getName(), routeSet);

        routeEditor.apply();
    }

    // delete a route
    public void deleteRoute(String name) {
        routeEditor.remove(name);
        routeEditor.apply();
    }

    // delete all route
    public void clearRoutes() {
        routeEditor.clear();
        routeEditor.apply();
    }

}
