package edu.ucsd.cse110.walkwalkrevolution.firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import edu.ucsd.cse110.walkwalkrevolution.MeasurementConverter;
import edu.ucsd.cse110.walkwalkrevolution.R;
import edu.ucsd.cse110.walkwalkrevolution.notifications.NotificationFactory;

public class FirebaseFirestoreAdapter {
    private static final String TAG = FirebaseFirestoreAdapter.class.getSimpleName();

    private FirebaseFirestore db;

    public FirebaseFirestoreAdapter(FirebaseFirestore db) {
        this.db = db;
    }

    private static boolean isDocumentId(String ids[]) {
        return MeasurementConverter.isEven(ids.length);
    }

    private static boolean isCollectionId(String ids[]) {
        return !isDocumentId(ids);
    }

    private Object getDatabaseReference(String[] ids) {
        Object ref = db.collection(ids[0]);
        for (int i = 1; i < ids.length; i++) {
            if (MeasurementConverter.isEven(i)) {
                ref = ((DocumentReference) ref).collection(ids[i]);
            } else {
                ref = ((CollectionReference) ref).document(ids[i]);
            }
        }
        return ref;
    }

    public void add(String[] ids, Map<String, Object> data) {
        Log.d(TAG, "Adding data to Firestore: " + java.util.Arrays.toString(ids));
        assert ids.length > 0;
        Object ref = getDatabaseReference(ids);

        if (ref instanceof DocumentReference) {
            ((DocumentReference) ref).set(data, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        } else if (ref instanceof CollectionReference) {
            ((CollectionReference) ref).add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    public DocumentReference get(String[] ids) {
        assert isDocumentId(ids);
        return ((DocumentReference) getDatabaseReference(ids));
    }

    public CollectionReference collect(String[] ids) {
        assert isCollectionId(ids);
        return ((CollectionReference) getDatabaseReference(ids));
    }

    /**
     * Assume that the document has no nesting and is the only item that needs to be deleted
     */
    public void remove(String[] ids) {
        assert isDocumentId(ids);
        DocumentReference docRef = (DocumentReference) getDatabaseReference(ids);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document '" + Arrays.asList(ids) + "' successfully deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error deleting document '" + Arrays.asList(ids) + "'.");
            }
        });
    }

    public void subscribeListener(String[] ids, String key, Consumer<Object> listener) {
        assert isDocumentId(ids);
        DocumentReference docRef = (DocumentReference) getDatabaseReference(ids);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    listener.accept(snapshot.get(key));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public void notificationSubscribe(String[] ids, NotificationFactory factory, Context context, String currentUser) {
        assert isCollectionId(ids);
        CollectionReference colRef = (CollectionReference) getDatabaseReference(ids);
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d(TAG, "New Notification" + dc.getDocument().getData());
                            Map<String, Object> data = dc.getDocument().getData();
                            if (data.containsKey("emails")) {
                                if (((List<String>)data.get("emails")).contains(currentUser)) {
                                    Log.d(TAG, "Creating notification");
                                    String title = data.get("title").toString();
                                    String text = data.get("text").toString();
                                    String docName = dc.getDocument().getId();
                                    factory.createNotification(context, R.drawable.ic_launcher_foreground, title, text, Integer.parseInt(String.valueOf(docName.hashCode())));
                                }
                            }
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        });
    }
}
