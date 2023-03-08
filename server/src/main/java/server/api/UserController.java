package server.api;

import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repo;

    /**
     * Constructor for the UserController class
     * @param repo the user repository used
     */
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<User> getById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a new user
     * @param user the user to add
     * @return a ResponseEntity verifying the user is saved
     */
    @PostMapping(path = {"", "/"})
    @SuppressWarnings("unused")
    public ResponseEntity<User> add(@RequestBody User user) {
        User saved = repo.save(user);
        return ResponseEntity.ok(saved);
    }
}
