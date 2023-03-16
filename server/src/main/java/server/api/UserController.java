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

    @GetMapping(path = "/")
    public List<User> getAll() {
        return userService.getAll();
    }

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
}
