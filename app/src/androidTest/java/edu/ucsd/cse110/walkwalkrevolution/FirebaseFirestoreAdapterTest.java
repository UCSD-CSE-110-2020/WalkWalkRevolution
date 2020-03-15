package edu.ucsd.cse110.walkwalkrevolution;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FirebaseFirestoreAdapterTest {
    private static final String TAG = FirebaseFirestoreAdapterTest.class.getSimpleName();

    private FirebaseFirestoreAdapter adapter;

    private FirebaseFirestore db;
    public void setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        db = FirebaseFirestore.getInstance();
        adapter = new FirebaseFirestoreAdapter(db);
    }

    public void testAddDocument() throws ExecutionException, InterruptedException {
        String userId = "adalovelace";
        String notificationId = "invite";
        String[] ids = {"FirebaseFirestoreAdapterTest", userId, "notifications", notificationId};

        Map<String, Object> data = new HashMap<>();
        data.put("name", "john");
        data.put("date", "03/01/20");
        data.put("age", 13);
        adapter.add(ids, data);

        DocumentReference docRef = adapter.get(ids);
        Tasks.await(docRef.get().addOnSuccessListener(result -> {
            DocumentSnapshot document = (DocumentSnapshot)result;
            if (document.exists()) {
                System.out.println(TAG + "DocumentSnapshot data: " + document.getData());
                assertEquals("john", document.get("name"));
                assertEquals("03/01/20", document.get("date"));
                assertEquals(13, ((Long)(document.get("age"))).intValue());
            } else {
                fail("No such document");
            }
        }).addOnFailureListener(result -> {
            fail(result.toString());
        }));

        Tasks.await(db.collection(ids[0]).document(ids[1]).collection(ids[2]).document(ids[3])
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println(TAG + ": DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(TAG + ": Error deleting document" + e);
                        fail("Error deleting document" + e);
                    }
                }));
        Tasks.await(db.collection(ids[0]).document(ids[1])
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println(TAG + ": DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(TAG + ": Error deleting document" + e);
                        fail("Error deleting document" + e);
                    }
                }));
    }

    public void testSubscribeListener() {
        String userId = "adalovelace";
        String notificationId = "invite";
        String[] ids = {"FirebaseFirestoreAdapterTest", userId, "notifications", notificationId};

        adapter.subscribeListener(ids, "age", (item -> { assertEquals(13, ((Long)item).intValue());
        }));

        Map<String, Object> data = new HashMap<>();
        data.put("name", "john");
        data.put("date", "03/01/20");
        data.put("age", 13);
        adapter.add(ids, data);
    }
}
