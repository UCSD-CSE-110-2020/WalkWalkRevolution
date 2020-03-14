package edu.ucsd.cse110.walkwalkrevolution;

public class MockUser {

    private String name;
    private String email;
    private String uid;

    public MockUser(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUid() { return uid; }
}
