package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/boards")
public class BoardController {

    //private final BoardService boardService;
    private final BoardRepository repo;
    private SimpMessagingTemplate msgs;

    /**
     * Constructor for the BoardController class
     * @param repo the repository used
     * @param msgs the messages template
     */
    public BoardController(BoardRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
    }

    /**
     * A method that returns all boards on the address "localhost:8080/boards"
     *
     * @return a list of all existing boards
     */

    @GetMapping(path = {"", "/"})
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
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a new board
     * @param board the board to add
     * @return a ResponseEntity verifying the board is saved
     */
    @PostMapping(path ="/add")
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if (board == null || board.getName() == null|| board.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        msgs.convertAndSend("/topic/boards", board);
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * @param board the board that is modified
     * @return a ResponseEntity verifying the board is saved
     */
    @PutMapping(path ="/modify")
    public ResponseEntity<Board> putBoard(@RequestBody Board board) {
        if (board == null || board.getName() == null|| board.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
//        listeners.forEach((k,l) -> l.accept(board));
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);

    }

    /**
     * Deletes a board from the repo
     * @param id the id of the board to be removed
     * @return an error if the board does not exist in the repo
     */
    @DeleteMapping(path = "/delete/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<Board> removeBoard(@PathVariable("id") long id) {
        //if (boardService.delete(id)){
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok().build();
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Board deleted = repo.getById(id);
        repo.deleteById(id);
        return ResponseEntity.ok(deleted);
    }

    /**
     * Modifies the name of a board
     * @param id the id of the board to be modified
     * @param name the new name of the board
     * @return an error if the change cannot be made, ok otherwise
     */
    @PostMapping(path = "/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<Board> modifyName(@PathVariable("id") long id, @RequestBody String name) {
        //if(!boardService.changeName(boardService.getById(id), name)) {
        //    return ResponseEntity.badRequest().build();
        //}
        //return ResponseEntity.ok().build();
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Board newBoard = repo.getById(id);
        newBoard.setName(name);
        repo.save(newBoard);
        return ResponseEntity.ok(newBoard);
    }

    /**
     * Gets all the boards a user has joined
     * @param id the id of the user
     * @return a response entity containing all the boards a user has joined
     */
    @GetMapping("/user/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<List<Board>> getBoardsByUser(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsByUsers_Id(id)) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        msgs.convertAndSend("/topic/boardsRenameDeleteAdd", id);
        return ResponseEntity.ok(repo.findByUsers_Id(id));
    }

    /**
     * Updates a board
     * @param id the id of the board to be updated
     * @param board the new version of the board
     * @return a response entity containing the updated board, if the update is possible
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable("id") long id,
                                             @RequestBody Board board) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(board);
        msgs.convertAndSend("/topic/boardsUpdate", board);
        return ResponseEntity.ok(board);
    }
}