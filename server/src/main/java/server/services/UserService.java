package server.services;

import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

import java.util.List;

@Service
public class UserService extends GenericService<User> {

    /**
     * Constructor for the UserService
     * @param repo the repository used
     */
    @Autowired
    public UserService(UserRepository repo) {
        super(repo);
    }

    /**
     * @param user user to be added
     * @return null id the user is null or has a null field, else the user
     */
    public User add(User user) {
        if (user == null || user.getUsername() == null) {
            return null;
        }
        return repo.save(user);
    }

    /**
     * Changes the username of a given user
     * @param target the user whose name is to change
     * @param name the name that will substitute the old name
     * @return true if succesful false otherwise
     */
    public boolean changeName(User target, String name) {
        if (target != null && this.repo.existsById(target.getId())) {
            target.setUsername(name);
            return true;
        }
        return false;
    }

    /**
     * Checks whether there exists a user with a given username
     * @param username the username in search
     * @return true if-f there exists a user with the given username
     */
    public boolean usernameExists(String username) {
        UserRepository myRepo = (UserRepository) repo;
        return myRepo.existsByUsername(username);
    }

    /**
     * Getter for a user with a given username
     * @param username the username in search
     * @return the user if there is one; null otherwise
     */
    public User getByUsername(String username) {
        UserRepository myRepo = (UserRepository) repo;
        List<User> found = myRepo.findByUsernameIs(username);
        if (found.isEmpty()) {
            return null;
        } else {
            return found.get(0);
        }
    }
}
