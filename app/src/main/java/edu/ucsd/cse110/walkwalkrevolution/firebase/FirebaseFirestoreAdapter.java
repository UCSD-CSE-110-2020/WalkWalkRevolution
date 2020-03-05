package edu.ucsd.cse110.walkwalkrevolution.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;
import java.util.function.Consumer;

import edu.ucsd.cse110.walkwalkrevolution.MeasurementConverter;

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
        assert ids.length > 0;
        Object ref = getDatabaseReference(ids);

        if (ref instanceof DocumentReference) {
            ((DocumentReference) ref).set(data)
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
        } else if (ref instanceof CollectionReference){
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

    public Task<?> get(String[] ids) {
        assert isDocumentId(ids);
        return ((DocumentReference) getDatabaseReference(ids)).get();
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
}
