package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @After
    // runs after each test
    public void tearDown() throws Exception {
        userService.nukeUsers();
        // ensures all users are deleted after each test
    }


    @Test
    public void createUser() {
        // test for UserService.createUser

        // test if user gets created properly
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        // token is set
        Assert.assertNotNull(createdUser.getCreationDate());
        // creation date is set
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        // user created as OFFLINE - only goes ONLINE by login
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
        // i don't quite see the point of this tbh
    }


    @Test
    public void getUsers() {
        // test for UserService.getUsers

        // tests that all users can be gotten from database
        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        User testUser2 = new User();
        testUser2.setPassword("testPassword2");
        testUser2.setUsername("testUsername2");

        User testUser3 = new User();
        testUser3.setPassword("testPassword3");
        testUser3.setUsername("testUsername3");


        User createdUser = userService.createUser(testUser);
        User createdUser2 = userService.createUser(testUser2);
        User createdUser3 = userService.createUser(testUser3);
        // create 3 users, save to DB

        Iterable<User> users = userService.getUsers();
        // get all users
        ArrayList<User> list = new ArrayList<>();

        for (User user: users) {
            list.add(user);
        }
        // stash all users in an ArrayList


        Assert.assertEquals(createdUser, list.get(0));
        Assert.assertEquals(createdUser2, list.get(1));
        Assert.assertEquals(createdUser3, list.get(2));
        // ensure the created Users are the ones gotten from getUsers()
    }


    @Test
    public void nukeUsers() {
        // test for UserService.nukeUsers

        // tests that the delete-function specifically written for tests actually works
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setPassword("pw2");
        testUser2.setUsername("uname2");
        userService.createUser(testUser2);
        // create test users

        userService.nukeUsers();
        // delete all users

        Assert.assertEquals(0, userRepository.count());
        // assert that there is no user in the database
    }


    @Test
    public void getUserById() {
        // test for UserService.getUserById

        //
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);
        // create test user

        User u = userService.getUserById(testUser.getId());
        Assert.assertEquals(testUser, u);
        // assert that the user is findable by his ID
    }


    @Test
    public void getUserByToken() {
        // test for UserService.getUserByToken

        //
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);
        // create test user

        User u = userService.getUserByToken(testUser.getToken());
        Assert.assertEquals(testUser, u);
        // assert that the user is findable by his Token
    }


    @Test
    public void updateUser() {
        // test for UserService.updateUser

        //
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);
        //
        User testUser2 = new User();
        testUser2.setPassword("pw2");
        testUser2.setUsername("uname2");
        userService.createUser(testUser2);
        //
        User testUser3 = new User();
        testUser3.setPassword("pw3");
        testUser3.setUsername("uname3");
        userService.createUser(testUser3);
        // create test users


        User upUser = new User();
        upUser.setId(testUser.getId());
        // gets the Id to ensure updating is possible
        upUser.setUsername("USERNAME 1");
        upUser.setBirthdayDate("11.11.1992");
        userService.updateUser(upUser);
        // updating testUser

        User upUser2 = new User();
        upUser2.setId(testUser2.getId());
        // gets the Id to ensure updating is possible
        upUser2.setUsername("USERNAME 2");
        userService.updateUser(upUser2);
        // updating ONLY USERNAME for testUser

        User upUser3 = new User();
        upUser3.setId(testUser3.getId());
        // gets the Id to ensure updating is possible
        upUser3.setUsername(testUser3.getUsername());
        // gets the username to ensure updating is possible
        upUser3.setBirthdayDate("01.01.1881");
        userService.updateUser(upUser3);
        // updating ONLY USERNAME for testUser


        Assert.assertEquals("USERNAME 1", userRepository.findUserById(testUser.getId()).getUsername());
        Assert.assertEquals("11.11.1992", userRepository.findUserById(testUser.getId()).getBirthdayDate());
        // assert that both username and birthday have been updated for testUser

        Assert.assertEquals("USERNAME 2", userRepository.findUserById(testUser2.getId()).getUsername());
        Assert.assertNull(userRepository.findUserById(testUser2.getId()).getBirthdayDate());
        // assert that ONLY THE USERNAME has been updated for testUser2

        Assert.assertEquals("uname3", userRepository.findUserById(testUser3.getId()).getUsername());
        Assert.assertEquals("01.01.1881", userRepository.findUserById(testUser3.getId()).getBirthdayDate());
        // assert that ONLY THE BIRTHDAY has been updated for testUser
    }


    @Test
    public void loginUser() {
        // test for UserService.loginUser

        //
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);
        // created the testUser, basically performing the registration
        // state of the user is set to OFFLINE after registration

        User lUser = new User();
        lUser.setPassword("pw");
        lUser.setUsername("uname");
        // login credentials stored in loginUser

        userService.loginUser(lUser);
        // logging in user

        Assert.assertEquals("ONLINE",userRepository.findByUsername(testUser.getUsername()).getStatus().toString());
        // assert that user status has been updated to ONLINE
    }


    @Test
    public void logoutUser() {
        // test for UserService.logoutUser

        //
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);
        // created testUser, initial state is set to OFFLINE

        User lUser = new User();
        lUser.setPassword("pw");
        lUser.setUsername("uname");
        // login credentials stored in loginUser

        userService.loginUser(lUser);
        // logging in user, thus modifying state to ONLINE

        userService.logoutUser(testUser.getToken());
        // logging out user again

        Assert.assertEquals("OFFLINE", userRepository.findByUsername(testUser.getUsername()).getStatus().toString());
        // assert that after logout, the user state is set to OFFLINE
    }


    @Test
    public void allowAccess() {
        // test for UserService.allowAccess

        // tests whether a submitted token (from the localStorage) is still valid,
        // i.e belongs to a currently registered user
        User testUser = new User();
        testUser.setPassword("pw");
        testUser.setUsername("uname");
        userService.createUser(testUser);
        // created testUser, got token generated

        Boolean granted = userService.allowAccess(testUser.getToken());

        Assert.assertEquals(true, granted);
        // assert that it returns true if the token is valid
    }
}
