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
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;


// store all routes and load routes from user preferences
public class ResponseManager {
    private static final String TAG = ResponseManager.class.getSimpleName();

    private Context context;
    SharedPreferences teamSp;
    String teamId;

    ResponseManager(Context currentContext) {
        context = currentContext;
        teamId = Team.getTeam(context);
    }

    // load saved routes
    public void load(FirebaseFirestoreAdapter adapter, ListView list) {
        if (teamId.equals(context.getResources().getString(R.string.empty))) { // Make sure team exists
            ArrayList<String> empty = new ArrayList<>();
            empty.add("Not currently in a team!");
            MemberListAdapter customAdapter = new MemberListAdapter((Activity) context, empty, empty);
            list.setAdapter(customAdapter);
        } else {
            String[] ids = {"teams", teamId};
            DocumentReference ref = adapter.get(ids);

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {
                            Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does not exist, cannot get members from it.");
                        }
                        else {
                            Log.d(TAG, "Document '" + java.util.Arrays.toString(ids) + "' does exist, getting members from it.");
                            Map members = (Map) document.get("members");
                            Map invited = (Map) document.get("invited");
                            ArrayList<String> membersList = new ArrayList<String>(members.values());
                            ArrayList<String> invitedList = new ArrayList<String>(invited.values());
                            membersList.addAll(invitedList);
                            MemberListAdapter customAdapter = new MemberListAdapter((Activity) context, membersList, invitedList);
                            list.setAdapter(customAdapter);
                        }
                    }
                }
            });
        }
    }
}
