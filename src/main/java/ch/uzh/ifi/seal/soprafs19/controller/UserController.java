package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }


    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }
    // returns all Users via .findAll(). fine as is, not REST specification given.


    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        // @RequestBody binds incoming JSON to the object it annotates
        try {
            this.service.createUser(newUser);
            String url = "users/" + newUser.getId();

            return new ResponseEntity<>(url, HttpStatus.CREATED);
            // CREATED is status code 201
        }
        catch (Exception e) {
            String err = "username already taken!";
            return new ResponseEntity<>(err, HttpStatus.CONFLICT);
            // CONFLICT is status code 409
        }
    }


    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        try {
            User validatedUser = this.service.loginUser(user);
            if (validatedUser != null) {
                //valid credentials
                return new ResponseEntity<>(validatedUser, HttpStatus.OK);
            }
            else {
                // wrong credentials
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch (Exception e) {
            // something went terribly wrong
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = this.service.getUserById(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
            // OK is status code 200
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // here in the REST spec sheet it says is should return a string with the error.
        // however, since i need to return a Type User on successful request, i don't see
        // how i can return a String on an unsuccessful request. I will thus simply craft
        // an appropriate alert matching the returned http status on the client-side.

        // NOT_FOUND is status code 404
    }


    @PutMapping("/users/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
        // fixes CORS issues with PUT http request, we'll see if this also works for Heroku
        // or if another origins is needed
    public ResponseEntity<String> updateUser(@RequestBody User thisUser) {
        try {
            this.service.updateUser(thisUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            // NO_CONTENT is status code 204
        }
        catch (Exception e) {
            return new ResponseEntity<>("No user with that id was found. user data not updated.", HttpStatus.NOT_FOUND);
            // NOT_FOUND is status code 404
        }
    }


    @PutMapping("/logout")
    @CrossOrigin(origins = "http://localhost:3000")
    // fixes CORS issues with PUT http request, we'll see if this also works for Heroku
    // or if another origins is needed
    public ResponseEntity<String> logoutUser(@RequestBody String thisUserToken) {
        try {
            this.service.logoutUser(thisUserToken);
            return new ResponseEntity<>("successful logout", HttpStatus.NO_CONTENT);
            // NO_CONTENT is status code 204
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred whilst logging out.", HttpStatus.NOT_FOUND);
            // NOT_FOUND is status code 404
        }
    }

}
