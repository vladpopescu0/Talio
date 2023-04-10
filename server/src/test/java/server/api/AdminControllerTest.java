package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.testng.AssertJUnit.assertTrue;

public class AdminControllerTest {
    private TestBoardRepository boardRepo;
    private TestCardListRepository cardListRepo;
    private TestCardRepository cardRepo;
    private SimpMessagingTemplate msg;
    private MessageChannel channel;

    private AdminController cont;

    private static final User SOME_USER = new User("u");
    private static final String wrongPass = "definitelyWrongPassword";

    private String password;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        channel = (message, timeout) -> true;
        msg = new SimpMessagingTemplate(channel);
        boardRepo = new TestBoardRepository();
        cardListRepo = new TestCardListRepository();
        cardRepo = new TestCardRepository();
        cont = new AdminController(boardRepo, cardListRepo, cardRepo,msg);
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

    /**
     * Test for deleteBoard
     */
    @Test
    public void deleteNormalBoard() {
        Card c1 = new Card("c1");
        Card c2 = new Card("c2");
        Card c3 = new Card("c3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        cardRepo.save(c1);
        cardRepo.save(c2);
        cardRepo.save(c3);
        CardList cl1 = new CardList("List1");
        CardList cl2 = new CardList("List2");
        cl1.addCard(c1);
        cl2.addCard(c2);
        cl2.addCard(c3);
        cl1.setId((long) -1);
        cl2.setId((long) -2);
        cardListRepo.save(cl1);
        cardListRepo.save(cl2);
        Board board = new Board(SOME_USER, "Board");
        board.addList(cl1);
        board.addList(cl2);
        Board b1 = new Board(SOME_USER, "B");
        board.setId((long)-1);
        b1.setId((long)-2);
        boardRepo.save(b1);
        boardRepo.save(board);
        var actual = cont.deleteBoard(password, 1);
        assertEquals(actual.getStatusCode(), OK);
        assertTrue(actual.getBody());
        assertEquals(boardRepo.boards.size(), 1);
        assertEquals(cardListRepo.lists.size(), 0);
        assertEquals(cardRepo.cards.size(), 0);
    }
}
