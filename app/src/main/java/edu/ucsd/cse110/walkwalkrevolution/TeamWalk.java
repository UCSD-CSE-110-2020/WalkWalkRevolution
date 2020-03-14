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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;

public class TeamWalk {

    private static final String TAG = TeamWalk.class.getSimpleName();

    Context context;
    private String name, location, creator;
    private Timestamp time;


    // Team should be initialized with the creator's name and email
    public TeamWalk(Context context, String name, String location, Timestamp time, String creator) {
        this.context = context;
        this.name = name;
        this.location = location;
        this.time = time;
        this.creator = creator;
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter, String[] ids) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> walk = new HashMap<>();
        walk.put("name", name);
        walk.put("location", location);
        walk.put("time", time);
        walk.put("creator", creator);
        walk.put("status", false);
        walk.put("response", new HashMap<String, Boolean>());
        data.put("walk", walk);

        // Database structure is "teams/<Random UUID>"
        Log.d(TAG, "Adding walk ('" + name + ": " + creator + "') to the team document ('" + ids[1] + "')");
        adapter.add(ids, data);
    }

    /**
     * If the document exists, addToDatabase is a no-op
     */
    public void addToDatabase(FirebaseFirestoreAdapter adapter, Callback callback) {
        String teamId = Team.getTeam(context);
        String[] teamIds = {"teams", teamId};
        DocumentReference docRef = adapter.get(teamIds);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(teamIds) + "' does not exist, cannot create team walk.");
                    }
                    else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(teamIds) + "' does exist, creating team walk.");
                        overwriteAddToDatabase(adapter, teamIds);
                        callback.onCallback();
                    }
                }
            }
        });
        Log.d(TAG, "Exiting method");
    }
}
