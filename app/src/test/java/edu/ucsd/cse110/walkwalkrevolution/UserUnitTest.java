package edu.ucsd.cse110.walkwalkrevolution;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UserUnitTest {

    //Serve as a mock database
    MockDatabase DB;

    //Each ID is unique
    private String AmandaID = "A11111111";
    private String JerryID = "A22222222";
    private String KennyID = "A33333333";

    //Mock users
    private MockUser Amanda;
    private MockUser Jerry;
    private MockUser Kenny;

    @Before
    public void setup() throws Exception {

        Amanda = new MockUser("Amanda","amanda@ucsd.edu", AmandaID);
        Jerry = new MockUser("Jerry","jerry@ucsd.edu", JerryID);
        Kenny = new MockUser("Kenny","kenny@ucsd.edu", KennyID);
        Map<String, Object> users = new HashMap<String, Object>();
        users.put(AmandaID, Amanda);
        users.put(JerryID, Jerry);
        users.put(KennyID, Kenny);
        DB = new MockDatabase(null, users);
    }

    @Test
    public void testInfo() {

        //Check for Amanda's information
        assertEquals(Amanda.getName(), "Amanda");
        assertEquals(Amanda.getEmail(), "amanda@ucsd.edu");
        assertEquals(Amanda.getUid(), AmandaID);

        //Check for Jerry's information
        assertEquals(Jerry.getName(), "Jerry");
        assertEquals(Jerry.getEmail(), "jerry@ucsd.edu");
        assertEquals(Jerry.getUid(), JerryID);

        //Check for Kenny's information
        assertEquals(Kenny.getName(), "Kenny");
        assertEquals(Kenny.getEmail(), "kenny@ucsd.edu");
        assertEquals(Kenny.getUid(), KennyID);
    }

    @Test
    public void testDB() {

        MockUser verifier = (MockUser)DB.getUsers().get(AmandaID);

        //Check if db saved the Amanda properly
        assertEquals(verifier.getName(), "Amanda");
        assertEquals(verifier.getEmail(), "amanda@ucsd.edu");
        assertEquals(verifier.getUid(), AmandaID);

        verifier = (MockUser)DB.getUsers().get(JerryID);

        //Check if db saved the Jerry properly
        assertEquals(verifier.getName(), "Jerry");
        assertEquals(verifier.getEmail(), "jerry@ucsd.edu");
        assertEquals(verifier.getUid(), JerryID);

        verifier = (MockUser)DB.getUsers().get(KennyID);

        //Check if db saved the Kenny properly
        assertEquals(verifier.getName(), "Kenny");
        assertEquals(verifier.getEmail(), "kenny@ucsd.edu");
        assertEquals(verifier.getUid(), KennyID);
    }
}
