package server.api;

import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    //private final UserService userService;
    private final UserRepository repo;

    /**
     * Constructor for the UserController class
     * @param repo the user repository used
     */
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * gets all users in the repo
     * @return all the users as a list
     */
    @GetMapping(path = "/")
    @SuppressWarnings("unused")
    public List<User> getAll() {
        //return userService.getAll();
        return repo.findAll();
    }


    /**
     * Gets a user with a specific id
     * @param id the id in search
     * @return the user as a response entity if found,
     * a bad request page otherwise
     */
    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<User> getById(@PathVariable("id") long id) {
        //User user = userService.getById(id);
        //if (user == null) {
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok(user);
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Checks whether a string is null or empty
     * @param s the string in test
     * @return true if s == null or s == ''
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Adds a new user
     * @param user the user to add
     * @return a ResponseEntity verifying the user is saved
     */
    @PostMapping(path = "/add")
    @SuppressWarnings("unused")
    public ResponseEntity<User> add(@RequestBody User user) {
        //User added = userService.add(user);
        //if (added == null) {
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok(added);
        if (isNullOrEmpty(user.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        User saved = repo.save(user);
        return ResponseEntity.ok(saved);
    }

    /**
     * Modifies the name of a user
     * @param id the id of the user to be modified
     * @param name the new name of the user
     * @return an error if the change cannot be made, ok otherwise
     */
    @PostMapping(path = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<User> modifyName(@PathVariable("id") long id, @RequestBody String name) {
        //if (!userService.changeName(userService.getById(id), name)) {
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok().build();
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        User newUser = repo.getById(id);
        newUser.setUsername(name);
        repo.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    /**
     * Checks whether a user with a specific username exists, and returns it if so
     * @param name the username in search
     * @return a response entity with a user with the given username
     * if one exists; null otherwise
     */
    @GetMapping("/username/{name}")
    @SuppressWarnings("unused")
    public ResponseEntity<List<User>> getByUsername(@PathVariable("name") String name) {
        //if (!userService.usernameExists(name)) {
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok(userService.getByUsername(name));
        if (!repo.existsByUsername(name)) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        User b = repo.findByUsernameIs(name).get().get(0);
        return ResponseEntity.ok(repo.findByUsernameIs(name).get());
    }


}
