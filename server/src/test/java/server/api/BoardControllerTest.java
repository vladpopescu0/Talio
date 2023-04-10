package server.api;

import commons.Board;
import commons.CardList;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardControllerTest {
    private TestBoardRepository repo;
    private TestColorSchemeRepository csrepo;
    private BoardController sut;
    private SimpMessagingTemplate msg;
    private MessageChannel channel;
    private static final User SOME_USER = new User("u");
    private static final User SOME_OTHER_USER = new User("U");

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        channel = (message, timeout) -> true;
        msg = new SimpMessagingTemplate(channel);
        repo = new TestBoardRepository();
        csrepo = new TestColorSchemeRepository();
        sut = new BoardController(repo, msg, csrepo);
    }

    /**
     * Test for add
     */
    @Test
    public void addNullBoardTest() {
        var actual = sut.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addNullOrEmptyNameBoardTest() {
        var actual = sut.add(new Board(SOME_USER, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        var empty = sut.add(new Board(SOME_USER, ""));
        assertEquals(BAD_REQUEST, empty.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addActualBoardTest() {
        Board b = new Board(SOME_USER, "b");
        var actual = sut.add(b);
        assertEquals(b, actual.getBody());
    }

    /**
     * Test for add
     */
    @Test
    public void databaseIsUsed() {
        sut.add(new Board(SOME_USER, "n"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for getALl
     */
    @Test
    public void getAllTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        List<Board> boards = sut.getAll();
        assertEquals(boards.size(), 3);
        assertTrue(boards.contains(b1));
        assertTrue(boards.contains(b2));
        assertTrue(boards.contains(b3));
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    /**
     * Test for getById
     */
    @Test
    public void getByNegativeIdTest() {
        var actual = sut.getById(-2);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getById
     */
    @Test
    public void getByTooLargeIdTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        var actual = sut.getById(222);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getById
     */
    @Test
    public void getByIdTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        var actual1 = sut.getById(0);
        var actual2 = sut.getById(1);
        var actual3 = sut.getById(2);
        assertEquals(b1, actual1.getBody());
        assertEquals(b2, actual2.getBody());
        assertEquals(b3, actual3.getBody());
        assertTrue(repo.calledMethods.contains("findById"));
    }

    /**
     * Test for modifyName
     */
    @Test
    public void modifyNameNonExistingBoardTest() {
        var actual = sut.modifyName(22, "Bad");
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for modifyName
     */
    @Test
    public void modifyNameTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        var actual = sut.modifyName(2, "cc");
        assertEquals(actual.getBody().getName(), "cc");
        assertEquals(sut.getById(2).getBody().getName(), "cc");
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for removeBoard when board has negative id
     */
    @Test
    public void removeNegativeIdBoard() {
        var alert = sut.removeBoard(-1);
        assertEquals(BAD_REQUEST, alert.getStatusCode());
    }

    /**
     * Test for removeBoard
     */
    @Test
    public void removeNonExistingBoard() {
        var alert = sut.removeBoard(22);
        assertEquals(BAD_REQUEST, alert.getStatusCode());
    }

    /**
     * Test for removeBoard
     */
    @Test
    public void removeBoardTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        var actual = sut.removeBoard(2);
        assertEquals(actual.getBody(), b3);
        assertEquals(repo.boards.size(), 2);
        assertTrue(repo.calledMethods.contains("deleteById"));
    }

    /**
     * Test for updateBoard
     */
    @Test
    public void updateNonExistingBoardTest() {
        Board b1 = new Board(SOME_USER, "b");
        var actual = sut.updateBoard(22, b1);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for updateBoard
     */
    @Test
    public void updateBoardTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        b1.setId((long) -1);
        b2.setId((long) -2);
        b3.setId((long) -3);
        sut.add(b1);
        sut.add(b2);
        var actual = sut.updateBoard(1, b3);
        assertEquals(actual.getBody(), b3);
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for setBoardPassword
     */
    @Test
    public void setBoardPasswordTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        sut.add(b1);
        sut.add(b2);
        assertEquals(true, sut.setBoardPassword(1, "pass").getBody());
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for setBoardPassword with a wrong id
     */
    @Test
    public void setBoardPasswordTestWrongId() {
        Board b1 = new Board(SOME_USER, "b");
        sut.add(b1);
        assertEquals(400, sut.setBoardPassword(5, "pass").getStatusCodeValue());
    }

    /**
     * Test for removeBoardPassword
     */
    @Test
    public void removeBoardPasswordTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(true, sut.removeBoardPassword(1).getBody());
        assertTrue(repo.calledMethods.contains("save"));
    }


    /**
     * Test for removeBoardPassword with wrong id
     */
    @Test
    public void removeBoardPasswordTestWrongId() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(400, sut.removeBoardPassword(8).getStatusCodeValue());
    }



    /**
     * Test for checkBoardPassword
     */
    @Test
    public void checkBoardPasswordTest() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(true, sut.setBoardPassword(1, "pass").getBody());
        assertEquals(true, sut.setBoardPassword(2, "pass2").getBody());
        assertEquals(true, sut.checkBoardPassword(1, "pass").getBody());
        assertEquals(true, sut.checkBoardPassword(2, "pass2").getBody());
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for checkBoardPassword when one password is wrong
     */
    @Test
    public void checkBoardPasswordTestOneWrongPassword() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(true, sut.setBoardPassword(1, "password").getBody());
        assertEquals(true, sut.setBoardPassword(2, "pass2").getBody());
        assertEquals(true, sut.checkBoardPassword(2, "pass2").getBody());
        assertEquals(false, sut.checkBoardPassword(1, "pass").getBody());
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for checkBoardPassword when both passwords are wrong
     */
    @Test
    public void checkBoardPasswordTestTwoWrongPasswords() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(true, sut.setBoardPassword(1, "password").getBody());
        assertEquals(true, sut.setBoardPassword(2, "pass2").getBody());
        assertEquals(false, sut.checkBoardPassword(2, "password2").getBody());
        assertEquals(false, sut.checkBoardPassword(1, "pass").getBody());
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for checkBoardPassword when the ID of the board is wrong
     * and the password for the other situation is correct
     */
    @Test
    public void checkBoardPasswordTestOneWrongIdCorectPass() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(true, sut.setBoardPassword(1, "password").getBody());
        assertEquals(true, sut.setBoardPassword(0, "pass2").getBody());
        assertEquals(400, sut.checkBoardPassword(8, "pass2").getStatusCodeValue());
        assertEquals(true, sut.checkBoardPassword(1, "password").getBody());
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for checkBoardPassword when the ID of the board is wrong
     * and the password for the other situation is wrong
     */
    @Test
    public void checkBoardPasswordTestOneWrongIdWrongPass() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(true, sut.setBoardPassword(1, "password").getBody());
        assertEquals(400, sut.checkBoardPassword(8, "pass2").getStatusCodeValue());
        assertEquals(false, sut.checkBoardPassword(1, "passwo").getBody());
    }

    /**
     * Test for AddListToBoard
     */
    @Test
    public void addListToBoard() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        CardList c1 = new CardList("c1");
        CardList c2 = new CardList("c2");
        sut.add(b1);
        sut.add(b2);
        sut.addListToBoard(1, c1);
        sut.addListToBoard(1, c2);
        Board board = sut.getById(1).getBody();
        assertTrue(board.getList().contains(c1));
        assertTrue(board.getList().contains(c2));
    }

    /**
     * Test for AddListToBoard when the id of the board is wrong
     */
    @Test
    public void addListToBoardWrongId() {
        Board b1 = new Board(SOME_USER, "b");
        CardList c1 = new CardList("c1");
        sut.add(b1);
        assertEquals(400, sut.addListToBoard(10, c1).getStatusCodeValue());
    }

    /**
     * Test for AddListToBoard when only one id is wrong
     */
    @Test
    public void addListToBoardOneWrongId() {
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        CardList c1 = new CardList("c1");
        CardList c2 = new CardList("c2");
        sut.add(b1);
        sut.add(b2);
        sut.addListToBoard(1, c1);
        sut.addListToBoard(1, c2);
        Board board = sut.getById(1).getBody();
        assertEquals(400, sut.addListToBoard(10, c1).getStatusCodeValue());
        assertTrue(board.getList().contains(c1));
        assertTrue(board.getList().contains(c2));
    }

    /**
     * Test for AddListToBoard when the list is null
     */
    @Test
    public void addListToBoardNullName() {
        Board b1 = new Board(SOME_USER, "b");
        CardList c1 = new CardList();
        sut.add(b1);
        assertEquals(400, sut.addListToBoard(1, c1).getStatusCodeValue());
    }

    /**
     * Test for AddListToBoard when the list is null
     */
    @Test
    public void addListToBoardNull() {
        Board b1 = new Board(SOME_USER, "b");
        CardList c1 = null;
        sut.add(b1);
        assertEquals(400, sut.addListToBoard(1, c1).getStatusCodeValue());
    }

    /**
     * Test for AddListToBoard when the list is empty
     */
    @Test
    public void addListToBoardEmptyName() {
        Board b1 = new Board(SOME_USER, "b");
        CardList c1 = new CardList("");
        sut.add(b1);
        assertEquals(400, sut.addListToBoard(1, c1).getStatusCodeValue());
    }

    /**
     * Test for getByUser
     */
    @Test
    public void badUserTest() {
        SOME_USER.setId(2);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        var actual1 = sut.getBoardsByUser(-1);
        var actual2 = sut.getBoardsByUser(22);
        assertEquals(new ArrayList<>(), actual1.getBody());
        assertEquals(new ArrayList<>(), actual2.getBody());
    }

    /**
     * Test for getByUser
     */
    @Test
    public void getByUserTest() {
        SOME_USER.setId(1);
        SOME_OTHER_USER.setId(2);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_OTHER_USER, "bb");
        Board b3 = new Board(SOME_USER, "c");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        var actual1 = sut.getBoardsByUser(1);
        var actual2 = sut.getBoardsByUser(2);
        assertEquals(actual1.getBody().size(), 2);
        assertEquals(actual2.getBody().size(), 1);
        assertTrue(actual1.getBody().contains(b1));
        assertTrue(actual1.getBody().contains(b3));
        assertTrue(actual2.getBody().contains(b2));
        assertTrue(repo.calledMethods.contains("findByUsers_Id"));
    }

    /**
     * put board test
     */
    @Test
    public void putBoardTest() {
        SOME_USER.setId(1);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "newBoard");
        b1.setId(8L);
        b2.setId(8L);
        sut.add(b1);
        var actual = sut.putBoard(b2);
        assertEquals(actual.getBody(), b2);
    }

    /**
     * put board no name test
     */
    @Test
    public void putBoardEmptyNameTest() {
        SOME_USER.setId(1);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "");
        b1.setId(99L);
        b2.setId(99L);
        sut.add(b1);
        var actual = sut.putBoard(b2);
        assertEquals(actual.getStatusCodeValue(), 400);
    }

    /**
     * put board null name test
     */
    @Test
    public void putBoardNullNameTest() {
        SOME_USER.setId(1);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, null);
        b1.setId(99L);
        b2.setId(99L);
        sut.add(b1);
        var actual = sut.putBoard(b2);
        assertEquals(actual.getStatusCodeValue(), 400);
    }

    /**
     * put null board test
     */
    @Test
    public void putBoardNullTest() {
        Board b2 = null;
        var actual = sut.putBoard(b2);
        assertEquals(actual.getStatusCodeValue(), 400);
    }
}
