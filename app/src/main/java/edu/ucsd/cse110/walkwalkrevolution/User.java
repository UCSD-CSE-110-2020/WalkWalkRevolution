package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static java.lang.Thread.sleep;

public class User {

    private static final String TAG = User.class.getSimpleName();

    private String name;
    private String email;
    private String uid;


    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("uid", uid);

        // Database structure is "users/<NAME> <UID>"
        String[] ids = {"users", name + " " + uid};
        Log.d(TAG, "Adding new user to the database as a document called '" + name + " " + uid + "'");
        adapter.add(ids, data);
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

    /**
     * If the document exists, addToDatabase is a no-op
     */
    public void addToDatabase(FirebaseFirestoreAdapter adapter) {
        String[] ids = {"users", name + " " + uid};
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
