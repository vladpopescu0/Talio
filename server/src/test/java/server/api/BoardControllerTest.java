package server.api;

import commons.Board;
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
        sut = new BoardController(repo, msg);
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
        var actual = sut.add(new Board(SOME_USER,null));
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
    public void putBoardTest(){
        SOME_USER.setId(1);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, "newBoard");
        b1.setId(8L);
        b2.setId(8L);
        sut.add(b1);
        var actual = sut.putBoard(b2);
        assertEquals(actual.getBody(),b2);
    }

    /**
     * put board no name test
     */
    @Test
    public void putBoardNoNameTest(){
        SOME_USER.setId(1);
        Board b1 = new Board(SOME_USER, "b");
        Board b2 = new Board(SOME_USER, null);
        b1.setId(99L);
        b2.setId(99L);
        sut.add(b1);
        var actual = sut.putBoard(b2);
        assertEquals(actual.getStatusCodeValue(),400);
    }
}
