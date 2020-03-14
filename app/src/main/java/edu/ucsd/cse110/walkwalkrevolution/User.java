package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;

public class User implements Serializable {

    private static final String TAG = User.class.getSimpleName();

    private String name;
    private String nickname;
    private String email;
    private String uid;
    private String databaseId;

    private FirebaseFirestoreAdapter adapter;
    private String[] ids;

    public User() {}

    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public User(FirebaseFirestoreAdapter adapter, String name, String email, String uid) {
        this.adapter = adapter;
        this.name = name;
        this.nickname = name;
        this.email = email;
        this.uid = uid;
        this.databaseId = name + " " + uid;

        // Database structure is "/users/<NAME> <UID>"
        this.ids = new String[]{"users", databaseId};
    }

    public User(FirebaseFirestoreAdapter adapter, FirebaseUser user) {
        this(adapter, user.getDisplayName(), user.getEmail(), user.getUid());
        // Attempt to load from database to see if the nickname exists
        load(adapter, ids);
    }

    public User(FirebaseFirestoreAdapter adapter, String[] ids) {
        this();
        load(adapter, ids);
    }

    public void load(FirebaseFirestoreAdapter adapter, String[] ids) {
        this.adapter = adapter;
        this.ids = ids;
        adapter.get(ids).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' exists, loading user from it.");
                        name = (String) document.get("name");
                        nickname = (String) document.get("nickname");
                        if (nickname == null) nickname = name;
                        email = (String) document.get("email");
                        uid = (String) document.get("uid");
                        databaseId = name + " " + uid;
                        Log.d(TAG, "Loaded user as: name = '" + name +  "', nickname = '" + nickname + "', email = '" + email + "', uid = '" + uid + "', databaseID = '" + databaseId + "'.");
                    } else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist, cannot load user from it.");
                    }
                }
            }
        });
    }

    public void addPersonalRoutesFrom(User other) {
        String[] thisTeamRoutesId = ArrayUtil.append(this.ids, "team_routes");
        String[] othersPersonalRoutesId = ArrayUtil.append(other.ids, "personal_routes");
        Log.d(TAG, "Adding routes from " + Arrays.toString(othersPersonalRoutesId) + " to " + Arrays.toString(thisTeamRoutesId));
        adapter.collect(othersPersonalRoutesId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "Route document '" + Arrays.toString(ArrayUtil.append(othersPersonalRoutesId, document.getId())) + "' exists, adding it to the user's team routes.");
                        Route newRoute = document.toObject(Route.class);
                        String[] newTeamRouteId = ArrayUtil.append(thisTeamRoutesId, document.getId());
                        adapter.add(newTeamRouteId, newRoute);
                    }
                } else {
                    Log.d(TAG, "Routes collection '" + java.util.Arrays.toString(othersPersonalRoutesId) + "' does not exist, cannot add it to the user's team routes.");
                }
            }
        });
    }

    public void addRouteFrom(String[] ids) {
        String[] thisTeamRoutesId = ArrayUtil.append(this.ids, "team_routes");
        adapter.get(ids).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Route route = document.toObject(Route.class);
                    // Add the route to an identically named document inside this user's team routes
                    String docName = ids[ids.length - 1];
                    String[] newTeamRouteId = ArrayUtil.append(thisTeamRoutesId, docName);
                    adapter.add(newTeamRouteId, route);
                }
            }
        });
    }

    @Override
    public String toString() {
        return "User: name = '" + name +  "', nickname = '" + nickname + "', email = '" + email + "', uid = '" + uid + "', databaseID = '" + databaseId + "'.";
    }

    public void updateCreatorOfPersonalRoutes(Callback.NoArg callback) {
        String[] thisPersonalRoutesId = ArrayUtil.append(this.ids, "personal_routes");
        adapter.collect(thisPersonalRoutesId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "Route document '" + Arrays.toString(ArrayUtil.append(thisPersonalRoutesId, document.getId())) + "' exists, updating its creator");
                        Route route = document.toObject(Route.class);
                        route.setCreator(nickname);
                        String[] newTeamRouteId = ArrayUtil.append(thisPersonalRoutesId, document.getId());
                        adapter.add(newTeamRouteId, route);
                    }
                    callback.call();
                } else {
                    Log.d(TAG, "Routes collection '" + java.util.Arrays.toString(thisPersonalRoutesId) + "' does not exist, cannot update the creator");
                }
            }
        });
    }

    // Sets the nickname and updates the creator field of each of the personal routes
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getDatabaseId() { return databaseId; }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("nickname", nickname);
        data.put("email", email);
        data.put("uid", uid);

        Log.d(TAG, "Adding new user to the database as a document called '" + name + " " + uid + "'");
        adapter.add(ids, data);
    }

    /**
     * If the document exists, addToDatabase is a no-op
     */
    public void addToDatabase(FirebaseFirestoreAdapter adapter) {
        DocumentReference docRef = adapter.get(ids);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist, adding it.");
                        overwriteAddToDatabase(adapter);
                    } else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' already exists, not adding it.");
                    }
                }
            }
        });
    }

    public void createTeam(Context context) {
        SharedPreferences teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        SharedPreferences.Editor teamSpEdit = teamSp.edit();

        Team team = new Team(this);
        String teamId = team.addToDatabase(WalkWalkRevolutionApplication.adapter);
        teamSpEdit.putString("teamId", teamId);
        teamSpEdit.commit();

        // appUser.addTeamToDatabase(WalkWalkRevolutionApplication.adapter, teamId);
    }

    public static User getUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String uid = user.getUid();
        return new User(name, email, uid);
    }

//    public void addTeamToDatabase(FirebaseFirestoreAdapter adapter, String teamId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("team", teamId);
//
//        // Database structure is "users/<NAME> <UID>"
//        String[] ids = {"users", name + " " + uid};
//        Log.d(TAG, "Adding team ('" + teamId + "') to the user document ('" + name + " " + uid + "')");
//        adapter.add(ids, data);
//    }
}
