package edu.ucsd.cse110.walkwalkrevolution;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

public class Team {

    private static final String TAG = Team.class.getSimpleName();

    private Map<String, String> users;
    private String cName, cEmail; // Creator info


    // Team should be initialized with the creator's name and email
    public Team(User user) {
        cName = user.getName();
        cEmail = user.getEmail();
        users = new HashMap<>();
        users.put(cEmail, cName);
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter, String[] ids) {
        Map<String, Object> data = new HashMap<>();
        data.put("members", users);
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
                    if (document.exists()) {
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
}
