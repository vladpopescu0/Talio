package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import java.util.List;

@RestController
@RequestMapping("api/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * Constructor for the BoardController class
     * @param boardService the board service used
     */
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * A method that returns all boards on the address "localhost:8080/boards"
     *
     * @return a list of all existing boards
     */

    @GetMapping(path = {"", "/"})
    public List<Board> getAll() {
        return boardService.getAll();
    }

    /**
     * Show a board on "localhost:8080/boards/{name}"
     * @param id the id of the board we are searching for
     * @return the board with the current name, if there is one.
     * If there isn't one, an error page comes up.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        Board board = boardService.getById(id);
        if (board == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(board);
    }

    /**
     * Adds a new board
     * @param board the board to add
     * @return a ResponseEntity verifying the board is saved
     */
    @PostMapping(path ="/add")
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board added = boardService.add(board);
        if(added == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(added);
    }

    /**
     * @param board the board that is modified
     * @return a ResponseEntity verifying the board is saved
     */
    @PutMapping(path ="/modify")
    public ResponseEntity<Board> putBoard(@RequestBody Board board) {
        Board added = boardService.add(board);
        if(added == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(added);
    }

    /**
     * Deletes a board from the repo
     * @param id the id of the board to be removed
     * @return an error if the board does not exist in the repo
     */
    @DeleteMapping(path = "/delete/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<Board> removeBoard(@PathVariable("id") long id) {
        if (boardService.delete(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
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
        if(!boardService.changeName(boardService.getById(id), name)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
