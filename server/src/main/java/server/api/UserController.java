package server.api;

import commons.User;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/users")
public class UserController {

    //private final UserService userService;
    private final UserRepository repo;

    private int passHash;

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
        if (user == null || isNullOrEmpty(user.getUsername())) {
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

    /**
     * Checks whether given password classifies user as an admin
     * @param pass the password
     * @return True if the password is correct, false otherwise
     */
    @GetMapping("/admin/{pass}")
    public ResponseEntity<Boolean> checkAdmin(@PathVariable("pass") String pass){
        if (pass.hashCode() == passHash){
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Creates a random password and sets the passHash variable to the hash of the password
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startup(){
        String password = generateRandomPassword(12);
        passHash = password.hashCode();
        System.out.println(password);
    }

    /**
     * Generates a random String of alphanumeric characters to be used as a password
     * @param length The length of the required string
     * @return A random String of characters
     */
    private String generateRandomPassword(int length) {
        Random rnd = new Random();
        String out = "";
        final String alphaNumeric =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++){
            out += alphaNumeric.charAt(rnd.nextInt(alphaNumeric.length()));
        }
        return out;
    }



}
