package server.database;

import commons.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds all the users with a specific username
     * @param name the name in search
     * @return the list of all users with that username
     */
    List<User> findByUsernameIs(String name);

    /**
     * Checks whether a user with a given username exists
     * @param name the name of the user in search
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsername(String name);
}
