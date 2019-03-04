package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
        // returns all users in this UserRepository
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        // TODO: will need to include creation date
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User getUserById(Long id) {
        return this.userRepository.findUserById(id);
    }
    // gets a single User, identified via his User ID

    public User updateUser(User thisUser) {
        Long id = thisUser.getId(); // consistent across both the updated and "old" user data
        User updatedUser = this.userRepository.findUserById(id);
        // 'this' needed?

        if (!thisUser.getUsername().equals(updatedUser.getUsername())) {
            updatedUser.setBirthdayDate(thisUser.getBirthdayDate());
            // if the old entry is different, then it is overwritten
        }
        if (!thisUser.getBirthdayDate().equals(updatedUser.getBirthdayDate())) {
            updatedUser.setBirthdayDate(thisUser.getBirthdayDate());
            // if the old entry is different, then it is overwritten
        }

        userRepository.save(updatedUser); // this might be bad
        return updatedUser;
        // returns the updated User with the new data. I don't want to simply PUT 'thisUser',
        // since there might be other entries modified besides username & birthday.
    }



}
