package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
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
    // returns all Users via .findAll()


    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        // @RequestBody binds incoming JSON to the object it annotates
        return this.service.createUser(newUser);
    }
    // creates new User


    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id) {
        // keep an eye on this @PathVariable
        return this.service.getUserById(id);
    }



    



    // TODO
    // looks like all the functionality used here is supposed to be Provided by the UserService class, which in turn
    // uses the helpers of the User class and UserRepository class


    // S1
    // POST User - predone (me thinks) - BUT, 409 not done yet and currently available to non-Users

    // S2
    // GET User - find a user via his ID, display user profile (if found)
    // GET User - response if user with ID was not found

    // S3
    // PUT User - update user profile entries
    // PUT User - response if no user with given ID is found


}
