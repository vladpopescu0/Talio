package server.api;

import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for the UserController class
     * @param userService the user controller used
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * gets all users in the repo
     * @return all the users as a list
     */
    @GetMapping(path = "/")
    public List<User> getAll() {
        return userService.getAll();
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
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Adds a new user
     * @param user the user to add
     * @return a ResponseEntity verifying the user is saved
     */
    @PostMapping(path = "/add")
    @SuppressWarnings("unused")
    public ResponseEntity<User> add(@RequestBody User user) {
        User added = userService.add(user);
        if (added == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(added);
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
        if (!userService.changeName(userService.getById(id), name)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Checks whether a user with a specific username exists, and returns it if so
     * @param name the username in search
     * @return a response entity with a user with the given username
     * if one exists; null otherwise
     */
    @GetMapping("/{name}")
    @SuppressWarnings("unused")
    public ResponseEntity<User> getByUsername(@PathVariable("name") String name) {
        return ResponseEntity.ok(userService.getByUsername(name));
    }
}
