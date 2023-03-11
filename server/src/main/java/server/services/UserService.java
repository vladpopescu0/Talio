package server.services;

import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

@Service
public class UserService extends GenericService<User> {

    @Autowired
    public UserService(UserRepository repo) {
        super(repo);
    }

    /**
     * @param user user to be added
     * @return null id the user is null or has a null field, else the user
     */
    public User add(User user) {
        if (user == null || user.getBoardList() == null
                || user.getUsername() == null) {
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

}
