package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

public class WalkWalkRevolutionApplication extends Application {
    public static StepCountData stepCount = new StepCountData();
    public static FirebaseFirestoreAdapter adapter;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        adapter = new FirebaseFirestoreAdapter(db);
    }
}
