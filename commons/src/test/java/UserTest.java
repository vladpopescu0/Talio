import commons.Board;
import commons.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    /**
     * Test for Constructor
     */
    @Test
    public void constructorTest() {
        User u = new User("u");
        assertEquals(u.getUsername(), "u");
        assertEquals(u.getBoardList(), new ArrayList<>());
    }

    /**
     * Test for getUsername
     */
    @Test
    public void getUsernameTest() {
        User u = new User("u");
        assertEquals("u", u.getUsername());
    }

    /**
     * Test for setUsername
     */
    @Test
    public void setUsernameTest() {
        User u = new User("u");
        u.setUsername("u1");
        assertEquals("u1", u.getUsername());
    }

    /**
     * Test for getBoards
     */
    @Test
    public void getBoardsTest() {
        User u = new User("u");
        assertEquals(u.getBoardList(), new ArrayList<>());
    }

    /**
     * Test for addBoard
     */
    @Test
    public void addBoardTest() {
        User u = new User("u");
        Board b = new Board(u, "b");
        u.addBoard(b);
        assertTrue(u.getBoardList().contains(b));
        u.addBoard(b);
        assertEquals(u.getBoardList().size(), 1);
        Board b1 = new Board(u, "11");
        u.addBoard(b1);
        assertEquals(u.getBoardList().size(), 2);
        assertTrue(u.getBoardList().contains(b1));
    }

    /**
     * Test for removeBoard
     */
    @Test
    public void removeBoardTest() {
        User u = new User("u");
        Board b = new Board(u, "b");
        u.addBoard(b);
        u.removeBoard(b);
        assertFalse(u.getBoardList().contains(b));
        Board b1 = new Board(u, "11");
        u.addBoard(b);
        u.removeBoard(b1);
        assertEquals(u.getBoardList().size(), 1);
    }

    /**
     * Test for equals
     */
    @Test
    public void equalsUsersTest() {
        User u = new User("u");
        User u1 = new User("u");
        assertEquals(u, u1);
    }

    /**
     * Test for equals
     */
    @Test
    public void notequalsUsersTest() {
        User u = new User("u");
        User u1 = new User("u1");
        assertNotEquals(u, u1);
    }

    /**
     * Test for hashCode
     */
    @Test
    public void equalHashCodes() {
        User u = new User("u");
        User u1 = new User("u");
        assertEquals(u.hashCode(), u1.hashCode());
    }

    /**
     * Test for hashCode
     */
    @Test
    public void notEqualHashCode() {
        User u = new User("u");
        User u1 = new User("u1");
        assertNotEquals(u.hashCode(), u1.hashCode());
    }
}
