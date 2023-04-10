package server.api;

import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class AdminControllerTest {
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;
    private TestCardRepository cardRepo;

    private AdminController cont;

    private static final User SOME_USER = new User("u");
    private static final String wrongPass = "definitelyWrongPassword";

    private String password;
    private MessageChannel channel;
    private SimpMessagingTemplate msgs;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        channel = (message, timeout) -> true;
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        cardRepo = new TestCardRepository();
        msgs = new SimpMessagingTemplate(channel);
        cont = new AdminController(boardRepo, cardListRepo, cardRepo,msgs);
        password = cont.startup();
    }

    /**
     * Tests that the generated passwords are random,
     * and that old passwords cannot be used
     */
    @Test
    public void randomPassCheck(){
        String newPass = cont.startup();
        assertNotEquals(password, newPass);
        var actual = cont.checkAdmin(password);
        assertEquals(false, actual.getBody());
    }

    /**
     * Tests checking admin privileges with an incorrect password
     */
    @Test
    public void checkWrongPass(){
        var actual = cont.checkAdmin(wrongPass);
        assertEquals(false, actual.getBody());
    }

    /**
     * Tests checking admin privileges with the correct password
     */
    @Test
    public void checkCorrectPass(){
        var actual = cont.checkAdmin(password);
        assertEquals(true, actual.getBody());
    }

    /**
     * Tests deleting a board from the board repository
     */
    @Test
    public void deleteBoard(){
        Board b = new Board(SOME_USER, "b");
        boardRepo.save(b);
        cont.deleteBoard(password, b.getId());
        assertEquals(0, boardRepo.boards.size());
    }

    /**
     * Tests deleting a board with the wrong admin password
     */
    @Test
    public void deleteBoardWrongPass(){
        Board b = new Board(SOME_USER, "b");
        boardRepo.save(b);
        var actual = cont.deleteBoard(wrongPass, b.getId());
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertEquals(1, boardRepo.boards.size());
    }

    /**
     * Tests deleting a board that doesn't exist
     */
    @Test
    public void deleteNonExistentBoard(){
        var actual = cont.deleteBoard(password, 271);
        assertEquals(false, actual.getBody());
        assertEquals(0, boardRepo.boards.size());
    }
}
