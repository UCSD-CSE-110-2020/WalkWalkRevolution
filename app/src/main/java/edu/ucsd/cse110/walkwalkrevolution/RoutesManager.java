package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;


// store all routes and load routes from user preferences
public class RoutesManager {
    private static String TAG = RoutesManager.class.getSimpleName();

    private Context context;
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

    // load routes from Firebase
    // load saved routes from Firebase, assuming that the ids specify a collection with documents
    // that are each serialized Route classes
    public void loadAllFromFirebase(String[] ids, ListView listView, Context context) {
        ArrayList<Route> firebaseRoutes = new ArrayList<>();
        WalkWalkRevolutionApplication.adapter.collect(ids)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Route newRoute = document.toObject(Route.class);
                                firebaseRoutes.add(newRoute);
                            }
                            Log.d(TAG, "Firebase routes size: " + Integer.toString(firebaseRoutes.size()));
                            Log.d(TAG, "Teammates routes: " + Arrays.toString(firebaseRoutes.toArray()));
                            RouteListAdapter customAdapter = new RouteListAdapter((Activity) context, firebaseRoutes);
                            listView.setAdapter(customAdapter);
                            // Setup onclick
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    gotoRoute(firebaseRoutes.get(position), context);
                                }
                            });
                        }
                    }
                });
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
            lastRun = Calendar.getInstance();
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

        loadedRoute.setCreator(savedRoutesFeatures.getString(
                route.getKey() + R.string.creator_tag, ""));
        return loadedRoute;
    }

    // add a new route
    public void addRoute(Route newRoute) {
        String startingLocation = newRoute.getStartingPoint();
        infoRouteEditor.putString(newRoute.getName(), startingLocation);

        int steps = newRoute.getSteps();
        featureRouteEditor.putInt(newRoute.getName() + R.string.steps_tag, steps);
        float distance = newRoute.getDistance();
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

        String creator = newRoute.getCreator();
        featureRouteEditor.putString(newRoute.getName() + R.string.creator_tag, creator);

        String path = newRoute.getFeatures().get(0);
        featureRouteEditor.putString(newRoute.getName() + R.string.path_tag, path);
        String terrain = newRoute.getFeatures().get(1);
        featureRouteEditor.putString(newRoute.getName() + R.string.terrain_tag, terrain);
        String enviroment = newRoute.getFeatures().get(2);
        featureRouteEditor.putString(newRoute.getName() + R.string.environment_tag, enviroment);
        String surface = newRoute.getFeatures().get(3);
        featureRouteEditor.putString(newRoute.getName() + R.string.surface_tag, surface);
        String difficulty = newRoute.getFeatures().get(4);
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
        featureRouteEditor.remove(name + R.string.creator_tag);
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

    // Add the route to the Firebase Firestore database as a document of the following signature:
    // "/users/<USER_ID>/personal_routes/<CREATOR_NAME> <ROUTE_NAME>"
    public void uploadRoute(Route route, FirebaseFirestoreAdapter adapter) {
        User appUser = new User(WalkWalkRevolutionApplication.adapter, FirebaseAuth.getInstance().getCurrentUser());
        String docName = route.getCreator() + "  " + route.getName();
        String[] routeId = {"users", appUser.getDatabaseId(), "personal_routes", docName};
        adapter.add(routeId, route);

        SharedPreferences teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        if (teamSp.contains("teamId")) {
            String teamId = teamSp.getString("teamId", context.getResources().getString(R.string.empty));
            String[] teamIds = {"teams", teamId};
            Log.d(TAG, "Loading team: " + Arrays.toString(teamIds));
            Team team = new Team();
            team.load(WalkWalkRevolutionApplication.adapter, teamIds, new Callback.NoArg() {
                @Override
                public void call() {
                    appUser.updateCreatorOfPersonalRoutes(new Callback.NoArg() {
                        @Override
                        public void call() {
                            team.forEachTeammateOf(appUser, new Callback.SingleArg<User>() {
                                @Override
                                public void call(User teammate) {
                                    teammate.addRouteFrom(routeId);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void gotoRoute(Route selectedRoute, Context context) {
        Intent intentRoute = new Intent(context, RouteActivity.class);
        intentRoute.putExtra("route", selectedRoute);
        context.startActivity(intentRoute);
    }
}
