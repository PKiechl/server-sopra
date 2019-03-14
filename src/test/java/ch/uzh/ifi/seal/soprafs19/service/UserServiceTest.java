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
        // create test user

        userService.nukeUsers();
        // delete all users

        Assert.assertEquals(0, userRepository.count());
        // assert that there is no user in the database
    }



}
