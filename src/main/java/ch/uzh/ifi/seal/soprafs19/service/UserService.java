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
import java.text.SimpleDateFormat;
import java.util.Date;

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
        newUser.setStatus(UserStatus.OFFLINE);
        // called in registration, now set to OFFLINE

        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        newUser.setCreationDate(date);
        // gets Creation Date (in a rather cumbersome fashion, but hey - it works)

        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User getUserById(Long id) {
        return this.userRepository.findUserById(id);
    }
    // gets a single User, identified via his User ID


    public void updateUser (User thisUser) {
        Long id = thisUser.getId(); // immutable, since primary key. thus valid identification method
        User updatedUser = this.userRepository.findUserById(id);

        if (thisUser.getBirthdayDate() != null) {
            // thus a birthday was provided, hence an update
            updatedUser.setBirthdayDate(thisUser.getBirthdayDate());
        }
        updatedUser.setUsername(thisUser.getUsername());
        // since the username needs to provided in either case, it is either new
        // and thus gets updated or it is the same, thus just overwritten with the same
        userRepository.save(updatedUser);
    }

    public User loginUser (User user) {
        User validatedUser = this.userRepository.findByUsername(user.getUsername());
        // search by username, since id is not delivered (only pw and uname)
        if (user.getUsername().equals(validatedUser.getUsername()) && user.getPassword().equals(validatedUser.getPassword())) {
            validatedUser.setStatus(UserStatus.ONLINE);
            // logging in sets user status to ONLINE
            return validatedUser;
        }
        return null;
    }

    public void logoutUser (String token) {
        User loggedoutUser = this.userRepository.findByToken(token);
        loggedoutUser.setStatus(UserStatus.OFFLINE);
        // sets the state to OFFLINE upon logout
    }


}
