package edu.ucsd.cse110.walkwalkrevolution;

import java.util.HashMap;
import java.util.Map;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TeamUnitTest {

    //Serve as a mock database
    MockDatabase DB;

    //Each ID is unique
    private String AmandaID = "A11111111";
    private String JerryID = "A22222222";
    private String KennyID = "A33333333";
    private String teamID = "@12345";

    //Mock users and team
    private MockUser Amanda;
    private MockUser Jerry;
    private MockUser Kenny;
    private MockTeam newTeam;

    @Before
    public void setup() throws Exception {

        DB = new MockDatabase(new HashMap<String, Object>(), new HashMap<String, Object>());
        Amanda = new MockUser("Amanda","amanda@ucsd.edu", AmandaID);
        Jerry = new MockUser("Jerry","jerry@ucsd.edu", JerryID);
        Kenny = new MockUser("Kenny","kenny@ucsd.edu", KennyID);

        newTeam = new MockTeam(Amanda);
        newTeam.addMember(Jerry);
        newTeam.addMember(Kenny);
        DB.addTeam(teamID, newTeam);
    }

    @Test
    public void testInfo() {

        //Check for creators' info
        assertEquals(newTeam.getCreator().get("amanda@ucsd.edu"), "Amanda");

        //Check for Jerry's info, who is member of the team
        MockUser member = (MockUser)newTeam.getMembers().get(JerryID);

        assertEquals(member.getName(), "Jerry");
        assertEquals(member.getEmail(), "jerry@ucsd.edu");
        assertEquals(member.getUid(), JerryID);

        //Check for Kenny's info, who is member of the team
        member = (MockUser)newTeam.getMembers().get(KennyID);

        assertEquals(member.getName(), "Kenny");
        assertEquals(member.getEmail(), "kenny@ucsd.edu");
        assertEquals(member.getUid(), KennyID);
    }

    @Test
    public void testDB() {

        //Get the team from Mock database with unique team id
        MockTeam verifier = DB.retrieveTeam(teamID);

        //Check the team is same as above
        assertEquals(verifier.getCreator().get("amanda@ucsd.edu"), "Amanda");

        MockUser member = (MockUser)verifier.getMembers().get(JerryID);

        assertEquals(member.getName(), "Jerry");
        assertEquals(member.getEmail(), "jerry@ucsd.edu");
        assertEquals(member.getUid(), JerryID);

        member = (MockUser)verifier.getMembers().get(KennyID);

        assertEquals(member.getName(), "Kenny");
        assertEquals(member.getEmail(), "kenny@ucsd.edu");
        assertEquals(member.getUid(), KennyID);
    }
}
