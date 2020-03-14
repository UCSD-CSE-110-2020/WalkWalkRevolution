package edu.ucsd.cse110.walkwalkrevolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class RoutesActivity extends AppCompatActivity {

    public static String TAG = "RoutesActivity";
    private Context context;
    SharedPreferences teamSp;
    String teamId;
    ArrayList<String> teamMembers = new ArrayList<String>();
    ArrayList<Route> teamRouteList = new ArrayList<Route>();
    RoutesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        Log.d(TAG, "Creating routes list activity");

        context = this;
        teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        teamId = teamSp.getString("teamId", context.getResources().getString(R.string.empty));

        Button bt_mainMenu = (Button) findViewById(R.id.bt_home);

        // logging for current route data
        SharedPreferences tempRoute  = getSharedPreferences("tempRoute", MODE_PRIVATE);
        Log.d("route","name " + tempRoute.getString("name", ""));
        Log.d("route","startingPoint " + tempRoute.getString("startingPoint", ""));
        Log.d("route","steps " + tempRoute.getString("steps", ""));
        Log.d("route","distance " + tempRoute.getString("distance", ""));
        Log.d("route","notes " + tempRoute.getString("notes", ""));
        Log.d("route","isFavorite " + tempRoute.getString("isFavorite", ""));
        Log.d("route","style " + tempRoute.getString("style", ""));
        Log.d("route","terrain " + tempRoute.getString("terrain", ""));
        Log.d("route","enviroment " + tempRoute.getString("environment", ""));
        Log.d("route","surface " + tempRoute.getString("surface", ""));
        Log.d("route","difficulty "  + tempRoute.getString("difficulty", ""));


        // check if user pressed main menu
        bt_mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainMenu();
            }
        });

        FloatingActionButton bt_newRoute = (FloatingActionButton) findViewById(R.id.bt_add);

        // check if user pressed new route
        bt_newRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNewRoute();
            }
        });

        // Setup RouteManager
        manager = new RoutesManager(this);

        // Route Testing (Alphebetical Order)
        //manager.clearRoutes();
        /* Manager Test
        manager.addRoute(new Route());
        manager.addRoute(new Route("kms", "londong"));
        manager.addRoute(new Route("wt00000f", "ok"));
        manager.addRoute(new Route("zebra", "ok"));
        manager.addRoute(new Route("aaaa", "ok"));
        manager.addRoute(new Route("adsad", "londong"));
        manager.addRoute(new Route("gadgdsg", "ok"));
        manager.addRoute(new Route("zczc", "ok"));
        manager.addRoute(new Route("aaadfdaaa", "ok"));
        */

        // Load personal route UI list
        ListView personalListView = (ListView) findViewById(R.id.routeList);

        //ArrayList<Route> routeList = manager.loadAll();
        User user = new User(WalkWalkRevolutionApplication.adapter, FirebaseAuth.getInstance().getCurrentUser());
        String[] personalRouteIds = {"users", user.getDatabaseId(), "personal_routes"};
        manager.loadAllFromFirebase(personalRouteIds, personalListView, this);

        // Load teammates
        loadTeammates(WalkWalkRevolutionApplication.adapter);

        // Load team route UI list
        ListView teamRouteListView = (ListView) findViewById(R.id.teamRouteList);
        // Load routes from database
        String[] teamRouteIds = {"users", user.getDatabaseId(), "team_routes"};
        manager.loadAllFromFirebase(teamRouteIds, teamRouteListView, this);
    }

    public void gotoMainMenu() {
        Intent intentMainMenu = new Intent(this, HomeActivity.class);
        intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMainMenu);
    }

    public void gotoNewRoute() {
        Intent intentNewRoute = new Intent(this, RouteNewActivity.class);
        intentNewRoute.putExtra("manuallyAddNewRoute", true);
        startActivity(intentNewRoute);
    }

    // see more info about a specific route
    public void gotoRoute(Route selectedRoute) {
        Intent intentRoute = new Intent(this, RouteActivity.class);
        intentRoute.putExtra("route", selectedRoute);
        startActivity(intentRoute);
    }

    // load teammates
    public void loadTeammates(FirebaseFirestoreAdapter adapter) {
        if (teamId.equals(context.getResources().getString(R.string.empty))) { // Make sure team exists
            teamMembers = new ArrayList<>();
            teamMembers.add("N/A");
        } else {
            String[] ids = {"teams", teamId};
            DocumentReference ref = adapter.get(ids);

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map data = (Map) document.get("members");
                            teamMembers = new ArrayList<String>(data.values());
                            Log.d(TAG, "Listing " + teamMembers.size() + " team members");
                            for(String name: teamMembers) {
                                Log.d(TAG, name);
                            }
                        }
                    }
                }
            });
        }
    }
}
