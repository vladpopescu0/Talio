package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardRepository repo;

    /**
     * Constructor for the BoardController class
     * @param repo the board repository used
     */
    @SuppressWarnings("unused")
    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * A method that returns all boards on the address "localhost:8080/boards"
     * @return a list of all existing boards
     */
    @GetMapping(path = {"", "/"})
    @SuppressWarnings("unused")
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * Show a board on "localhost:8080/boards/{name}"
     * @param id the id of the board we are searching for
     * @return the board with the current name, if there is one.
     * If there isn't one, an error page comes up.
     */
    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a new board
     * @param board the board to add
     * @return a ResponseEntity verifying the board is saved
     */
    @PostMapping(path = { "", "/"})
    @SuppressWarnings("unused")
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }
}
