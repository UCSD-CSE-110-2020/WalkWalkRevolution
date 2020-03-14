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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;

public class Invite {

    private static final String TAG = Invite.class.getSimpleName();

    Context context;
    private String from, name, email;


    // Team should be initialized with the creator's name and email
    public Invite(Context context, String from, String name, String email) {
        this.context = context;
        this.from = from;
        this.name = name;
        this.email = email;
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter, String[] ids, String teamId) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> invite = new HashMap<>();
        invite.put("teamId", teamId);
        invite.put("from", from);
        data.put("invite", invite);

        Log.d(TAG, "Adding invite to '" + teamId  + "' from '" + from + "' to the user document ('" + ids[1] + "')");
        adapter.add(ids, data);
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter, String[] ids, Map invited) {
        Map<String, Object> data = new HashMap<>();
        invited.put(email, name);
        data.put("invited", invited);

        // Database structure is "teams/<Random UUID>"
        Log.d(TAG, "Adding user ('" + email + ": " + name + "') to the team document ('" + ids[1] + "')");
        adapter.add(ids, data);
    }

    /**
     * If the document exists, addToDatabase is a no-op
     */
    public void addToDatabase(FirebaseFirestoreAdapter adapter, Callback callback) {
        Log.d(TAG, "Adding invite to the database");
        String[] collection = {"users"};
        CollectionReference usersRef = adapter.collect(collection);
        Query query = usersRef.whereEqualTo("email", email);
        String[] userIds = {"users", ""};
        SharedPreferences teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        String teamId = teamSp.getString("teamId", context.getResources().getString(R.string.empty));
        String[] teamIds = {"teams", teamId};

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    if (!query.isEmpty()) {
                        Log.d(TAG, "User with email '" + email + "' does exist, inviting.");
                        List<DocumentSnapshot> users = query.getDocuments();
                        userIds[1] = users.get(0).getId();
                        overwriteAddToDatabase(adapter, userIds, teamId);
                        inviteToTeam(adapter, teamIds, callback);
                    } else {
                        Log.d(TAG, "User with email '" + email + "' does not exist, cannot invite.");
                    }
                }
            }
        });
        Log.d(TAG, "Exiting method");
    }

    private void inviteToTeam(FirebaseFirestoreAdapter adapter, String[] teamIds, Callback callback) {
        Log.d(TAG, "Refreshing team's invited list");
        DocumentReference docRef = adapter.get(teamIds);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(teamIds) + "' does not exist, cannot invite.");
                    }
                    else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(teamIds) + "' does exist, inviting.");
                        Map invited = (Map) document.get("invited");
                        overwriteAddToDatabase(adapter, teamIds, invited);
                        callback.onCallback();
                    }
                }
            }
        });
    }
}
