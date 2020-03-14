package edu.ucsd.cse110.walkwalkrevolution;

import java.util.HashMap;
import java.util.Map;

public class MockDatabase {

    Map<String, Object> teams;
    Map<String, Object> users;

    MockDatabase(Map<String, Object> teams, Map<String, Object> users) {
        this.teams = teams;
        this.users = users;
    }

    public void addTeam(String ids, MockTeam team) {

        this.teams.put(ids, team);
    }

    public void addUsers(String id, MockUser user) {

        this.users.put(id, user);
    }

    public MockTeam retrieveTeam(String ids) {

        return (MockTeam)this.teams.get(ids);
    }

    public MockUser retrieveUser(String id) {

        return (MockUser)this.users.get(id);
    }

    public Map<String, Object> getTeams() {
        return this.teams;
    }

    public Map<String, Object> getUsers() {
        return this.users;
    }
}
