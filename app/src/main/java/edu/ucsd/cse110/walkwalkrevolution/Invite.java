package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class Invite {

    private static final String TAG = Invite.class.getSimpleName();

    Context context;
    private String name, email; // Creator info


    // Team should be initialized with the creator's name and email
    public Invite(Context context, String name, String email) {
        this.context = context;
        this.name = name;
        this.email = email;
    }

    public void overwriteAddToDatabase(FirebaseFirestoreAdapter adapter, String[] ids, Map invited) {
        Map<String, Object> data = new HashMap<>();
        invited.put(email, name)
        data.put("invited", invited);

        // Database structure is "teams/<Random UUID>"
        Log.d(TAG, "Adding user ('" + email + ": " + name + "') to the team document ('" + ids[1] + "')");
        adapter.add(ids, data);
    }

    /**
     * If the document exists, addToDatabase is a no-op
     */
    public void addToDatabase(FirebaseFirestoreAdapter adapter) {
        SharedPreferences teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        String teamId = teamSp.getString("teamId", context.getResources().getString(R.string.empty));
        String[] ids = {"teams", teamId};

        DocumentReference docRef = adapter.get(ids);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does exist, inviting.");
                        Map invited = (Map) document.get("members");
                        overwriteAddToDatabase(adapter, ids, invited);
                    } else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist, cannot invite.");
                    }
                }
            }
        });
    }
}
