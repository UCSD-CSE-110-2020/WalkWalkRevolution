package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


// store all routes and load routes from user preferences
public class RoutesManager {

    private Context context;
    private ArrayList<Route> routes;
    SharedPreferences sharedPreferences;

    RoutesManager(Context con) {
        context = con;
        sharedPreferences = context.getSharedPreferences("routes", MODE_PRIVATE);
    }

    // load routes from user preferences
    public void loadAll() {
        String numOfRoutesStr = sharedPreferences.getString("numOfRoutes", "0");
    }

    // add a new route
    public void newRoute() {

    }

    public void saveAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("test1", "1");
        editor.putString("test2", "2");
        editor.apply();
    }
}
