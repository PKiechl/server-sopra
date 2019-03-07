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
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        // @RequestBody binds incoming JSON to the object it annotates
        try {
            this.service.createUser(newUser);
            String url = "placeholder/" + newUser.getId();
            // TODO: update to real URL. might actually be of type Location, not sure

            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
            // CREATED is status code 201
        }
       catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
            // CONFLICT is status code 409
            // TODO: currently just returns status code 409 - needs to return the error itself too (?)
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        try {
            User validatedUser = this.service.loginUser(user);
            return new ResponseEntity<>(validatedUser, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = this.service.getUserById(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
            // OK is status cod 200
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // NOT_FOUND is status code 404
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@RequestBody User thisUser) {
        try {
            this.service.updateUser(thisUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            // NO_CONTENT is status code 204
            // TODO: currently able to alter user details from any /{id} for any user with a valid id.
            // not sure how to solve this yet
        }
        catch (Exception e) {
            return new ResponseEntity<>("some error", HttpStatus.NOT_FOUND);
            // NOT_FOUND is status code 404
        }
    }


}
