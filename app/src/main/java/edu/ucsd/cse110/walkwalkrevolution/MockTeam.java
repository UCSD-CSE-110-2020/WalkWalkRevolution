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

public class MockTeam {

    private Map<String, String> users;
    private Map<String, Object> members;
    private String cName, cEmail; // Creator info


    // Team should be initialized with the creator's name and email
    public MockTeam(MockUser user) {
        cName = user.getName();
        cEmail = user.getEmail();
        users = new HashMap<>();
        members = new HashMap<>();
        users.put(cEmail, cName);
        members.put(user.getUid(), user);
    }

    public void addMember(MockUser user) {

        this.members.put(user.getUid(), user);
    }

    public Map<String, String> getCreator() {
        return this.users;
    }

    public Map<String, Object> getMembers() {
        return this.members;
    }
}
