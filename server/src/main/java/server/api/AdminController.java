package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.Random;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    private int passHash;

    private BoardRepository boardRepo;
    private CardListRepository cardListRepo;
    private CardRepository cardRepo;

    /**
     * Constructor for AdminController class
     * @param boardRepo Board Repository
     * @param cardListRepo Card List Repository
     * @param cardRepo Card Repository
     */
    public AdminController(BoardRepository boardRepo, CardListRepository cardListRepo,
                           CardRepository cardRepo) {
        this.boardRepo = boardRepo;
        this.cardListRepo = cardListRepo;
        this.cardRepo = cardRepo;
    }

    /**
     * Checks whether given password classifies user as an admin
     * @param pass the password
     * @return True if the password is correct, false otherwise
     */
    @GetMapping("/{pass}")
    public ResponseEntity<Boolean> checkAdmin(@PathVariable("pass") String pass){
        if (pass.hashCode() == passHash){
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Uses admin privileges to delete a board
     * @param pass The admin password
     * @param bid The ID of the board to delete
     * @return True board was found and deleted, false otherwise
     */
    @GetMapping("/{pass}/delete/{bid}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable("pass") String pass,
                                               @PathVariable("bid") long bid){

        if (pass.hashCode() != passHash) return ResponseEntity.badRequest().build();
        if (!boardRepo.existsById(bid)) return ResponseEntity.ok(false);
        Board b = boardRepo.getById(bid);
        for (CardList c : b.getList()){
            cardListRepo.deleteById(c.getId());
        }
        boardRepo.deleteById(bid);
        return ResponseEntity.ok(true);
    }

    /**
     * Creates a random password and sets the passHash variable to the hash of the password
     * @return The generated password (for testing purposes)
     */
    @EventListener(ApplicationReadyEvent.class)
    public String startup(){
        String password = generateRandomPassword(12);
        passHash = password.hashCode();
        System.out.println(password);
        return password;
    }

    /**
     * Generates a random String of alphanumeric characters to be used as a password
     * @param length The length of the required string
     * @return A random String of characters
     */
    private String generateRandomPassword(int length) {
        Random rnd = new Random();
        StringBuilder out = new StringBuilder();
        final String alphaNumeric =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++){
            out.append(alphaNumeric.charAt(rnd.nextInt(alphaNumeric.length())));
        }
        return out.toString();
    }
}
