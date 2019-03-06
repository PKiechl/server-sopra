package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")  // @ Repository used to indicate the the class (here interface) provides the mechanisms
// for storage, retrieval, ...
public interface UserRepository extends CrudRepository<User, Long> {
												// stores User with ID tpye Long
	//User findByName(String name);
	User findByUsername(String username);
	User findByToken(String token);
	User findUserById(Long id);
}
