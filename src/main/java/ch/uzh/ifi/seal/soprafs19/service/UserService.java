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
        newUser.setStatus(UserStatus.ONLINE);

        String pattern = "dd.MM.yyyyy HH:mm:ss Z";
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

    public User updateUser (User thisUser) {
        Long id = thisUser.getId(); // consistent accross "old" and updated User
        User updatedUser = this.userRepository.findUserById(id);

        updatedUser.setBirthdayDate(thisUser.getBirthdayDate());
        updatedUser.setUsername(thisUser.getUsername());
        // overwrites current with new Username and BirthdayDate
        userRepository.save(updatedUser);
        return updatedUser;
    }

}
