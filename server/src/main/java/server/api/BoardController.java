package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardRepository repo;

    /**
     * Constructor for the BoardController class
     * @param repo the board repository used
     */
    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * A method that returns all boards on the address "localhost:8080/boards"
     * @return a list of all existing boards
     */
    @GetMapping(path = {"", "/"})
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * Show a board on "localhost:8080/boards/{name}"
     * @param name the name of the board we are searching for
     * @return the board with the current name, if there is one.
     * If there isn't one, an error page comes up.
     */
    @GetMapping("/{name}")
    public ResponseEntity<Board> getById(@PathVariable("name") String name) {
        if (!repo.existsById(name)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(name).get());
    }

    /**
     * Helper for the add method
     * @param s a String
     * @return true if-f the string is null or the empty String
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Adds a new board, if the creator exists and if the name of the board is unique
     * @param board the board to add
     * @return a ResponseEntity verifying the board is saved, or that the request is bad
     */
    @PostMapping(path = { "", "/"})
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if (board.getCreator() == null || isNullOrEmpty(board.getCreator().firstName)
        || isNullOrEmpty(board.getCreator().lastName) || repo.existsById(board.getName())) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }
}
