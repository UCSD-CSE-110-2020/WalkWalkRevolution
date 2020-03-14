package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;

public class Team {

    private static final String TAG = Team.class.getSimpleName();

    // User mapping is email to nick name
    private Map<String, String> members;

    // Creator info
    private String cName, cEmail;


    private FirebaseFirestoreAdapter adapter;
    private String[] ids;

    // Team should be initialized with the creator's name and email
    public Team(User user) {
        cName = user.getName();
        cEmail = user.getEmail();
        members = new HashMap<>();
        members.put(cEmail, cName);
    }

    public Team() {

    }

    // Retrieve team from Firestore ids
    public Team(FirebaseFirestoreAdapter adapter, String[] ids) {
        this();
        load(adapter, ids, null);
    }

    public void load(FirebaseFirestoreAdapter adapter, String[] ids, Callback.NoArg callback) {
        this.adapter = adapter;
        this.ids = ids;
        adapter.get(ids).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' exists, loading team from it.");
                        Map<String, Object> data = document.getData();
                        members = (Map<String, String>)data.get("members");
                    } else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist.");
                    }
                    if (callback != null) {
                        callback.call();
                    }
                }
            }
        });
    }

    // Adds a user to the database with the mapping of user to nickname
    public void addUser(User user, String nickname) {
        Log.d(TAG, "Adding user " + user.getName() + " as " + nickname + " to team of currently " + members.size() + " members");
        members.put(user.getEmail(), nickname);
        updateMembersInDatabase();
    }

    public void updateMembersInDatabase() {
        adapter.get(ids).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Updating team's members.");
                    //Map databaseMembers = (Map) document.get("members");
                    adapter.update(ids, "members", members);
                }
            }
        });
    }

    public void forEachTeammateOf(User user, Callback.SingleArg<User> callback) {
        Log.d(TAG, "Finding teammates of " + user.getName());
        Log.d(TAG, "All members: " + members.toString());
        members.forEach((email, nickname) -> {
            if (!email.equals(user.getEmail())) {
                // Query for the user with the stored email
                String[] collection = {"users"};
                CollectionReference usersRef = adapter.collect(collection);
                usersRef.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            if (!query.isEmpty()) {
                                Log.d(TAG, "User with email '" + email + "' does exist, adding to list of teammates.");
                                List<DocumentSnapshot> users = query.getDocuments();
                                String userId = users.get(0).getId();
                                String[] userIds = {"users", userId};
                                callback.call(new User(adapter, userIds));
                            } else {
                                Log.d(TAG, "User with email '" + email + "' does not exist, cannot find teammate.");
                            }
                        }
                    }
                });
            }
        });
        //Log.d(TAG, user.getName() + "'s " + teammates.size() + " teammates are: " + Arrays.toString(teammates.toArray()));
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter, String[] ids) {
        Map<String, Object> data = new HashMap<>();
        data.put("members", members);
        data.put("invited", new HashMap<>());

        // Database structure is "teams/<Random UUID>"
        Log.d(TAG, "Adding new team to the database as a document called '" + ids[1] + "'");
        Log.d(TAG, "Created with creator '" + cEmail + ": " + cName + "'");
        adapter.add(ids, data);
    }

    /**
     * If the document exists, addToDatabase is a no-op
     */
    public String addToDatabase(FirebaseFirestoreAdapter adapter) {
        String[] ids = {"teams", UUID.randomUUID().toString()};
        DocumentReference docRef = adapter.get(ids);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist, adding it.");
                        overwriteAddToDatabase(adapter, ids);
                    } else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' already exists, not adding it.");
                    }
                }
            }
        });
        return ids[1];
    }

    static boolean teamExists(Context context) {
        SharedPreferences teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        return !teamSp.contains("teamId");
    }

    static String getTeam(Context context) {
        SharedPreferences teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        return teamSp.getString("teamId", context.getResources().getString(R.string.empty));
    }
}
