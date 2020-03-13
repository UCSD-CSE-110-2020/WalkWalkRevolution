package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static android.content.Context.MODE_PRIVATE;


// store all routes and load routes from user preferences
public class MembersManager {
    private static final String TAG = MembersManager.class.getSimpleName();

    private Context context;
    SharedPreferences teamSp;
    String teamId;
    private ArrayList<String> members;

    MembersManager(Context currentContext) {
        context = currentContext;
        teamSp = context.getSharedPreferences(context.getResources().getString(R.string.team_store), MODE_PRIVATE);
        teamId = teamSp.getString("teamId", context.getResources().getString(R.string.empty));
    }

    // load saved routes
    public void load(FirebaseFirestoreAdapter adapter, ListView list) {
        if (teamId.equals(context.getResources().getString(R.string.empty))) { // Make sure team exists
            ArrayList<String> empty = new ArrayList<>();
            empty.add("Not currently in a team.");
            MemberListAdapter customAdapter = new MemberListAdapter((Activity) context, members);
            list.setAdapter(customAdapter);
        }

        String[] ids = {"teams", teamId};
        DocumentReference ref = adapter.get(ids);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does exist, getting members from it.");
                        Map data = (Map) document.get("members");
                        ArrayList<String> members = new ArrayList<String>(data.values());
                        MemberListAdapter customAdapter = new MemberListAdapter((Activity) context, members);
                        list.setAdapter(customAdapter);
                    } else {
                        Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist, cannot get members from it.");
                    }
                }
            }
        });
    }
}
