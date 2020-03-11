package edu.ucsd.cse110.walkwalkrevolution;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.walkwalkrevolution.firebase.FirebaseFirestoreAdapter;

public class User {

    private String name;
    private String email;
    private String uid;

    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public void addToDatabase(FirebaseFirestoreAdapter adapter) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("uid", uid);

        // Database structure is "users/<NAME> <UID>"
        String[] ids = {"users", name + " " + uid};
        adapter.add(ids, data);
    }
}
