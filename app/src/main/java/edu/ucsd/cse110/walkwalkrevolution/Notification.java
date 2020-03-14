package edu.ucsd.cse110.walkwalkrevolution;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

public class Notification {

    Notification (String title, String text, ArrayList<String> emails) {
        addToDatabase(WalkWalkRevolutionApplication.adapter, title, text, emails);
    }

    private String addToDatabase(FirebaseFirestoreAdapter adapter, String title,
                                 String text, ArrayList<String> emails) {
        String[] ids = {"notifications", UUID.randomUUID().toString()};
        DocumentReference docRef = adapter.get(ids);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", title);
                    data.put("text", text);
                    data.put("emails", emails);

                    adapter.add(ids, data);
                }
            }
        });
        return ids[1];
    }
}
