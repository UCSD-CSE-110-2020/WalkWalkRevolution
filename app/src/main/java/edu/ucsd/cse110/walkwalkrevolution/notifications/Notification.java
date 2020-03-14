package edu.ucsd.cse110.walkwalkrevolution.notifications;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.User;
import edu.ucsd.cse110.walkwalkrevolution.WalkWalkRevolutionApplication;
import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

public class Notification {
    private static final String TAG = Notification.class.getSimpleName();
    public static void sendNotification(FirebaseFirestoreAdapter adapter, List<String> toUserEmails, String title, String text) {
        Log.d(TAG, "Sending notification");
        String[] ids = {"notifications"};
        Map<String, Object> data = new HashMap<>();

        data.put("emails", toUserEmails);
        data.put("title", title);
        data.put("text", text);
        adapter.add(ids, data);
    }
}
