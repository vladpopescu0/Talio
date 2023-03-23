import commons.Board;
import commons.CardList;
import commons.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private static final User SOME_USER = new User("a");
    private static final User SOME_OTHER_USER = new User("u");


    /**
     * Test for constructor
     */
    @Test
    public void noListConstructorTest() {
        var b = new Board(SOME_USER, "b");
        assertEquals("b", b.getName());
        assertTrue(b.getUsers().contains(SOME_USER));
    }

    /**
     * Test for constructor
     */
    @Test
    public void listConstructorTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b = new Board(SOME_USER, cls, "b");
        assertEquals("b", b.getName());
        assertTrue(b.getUsers().contains(SOME_USER));
        assertEquals(cls, b.getList());
    }

    /**
     * Test for getUsers
     */
    @Test
    public void getUsersTest() {
        var b = new Board(SOME_USER, "b");
        List<User> users = new ArrayList<>();
        users.add(SOME_USER);
        assertEquals(users, b.getUsers());
    }

    /**
     * Test for addUser
     */
    @Test
    public void addUserTest() {
        var b = new Board(SOME_USER, "b");
        b.addUser(SOME_OTHER_USER);
        assertTrue(b.getUsers().contains(SOME_OTHER_USER));
    }

    /**
     * Test for removeUser
     */
    @Test
    public void removeUserTest() {
        var b = new Board(SOME_USER, "b");
        b.removeUser(SOME_USER);
        assertEquals(b.getUsers(), new ArrayList<>());
    }

    /**
     * Test for getName
     */
    @Test
    public void getNameTest() {
        var b = new Board(SOME_USER, "b");
        assertEquals(b.getName(), "b");
        assertNotEquals(b.getName(), "bb");
    }

    /**
     * Test for setName
     */
    @Test
    public void setNameTest() {
        var b = new Board(SOME_USER, "b");
        b.setName("c");
        assertNotEquals("b", b.getName());
        assertEquals("c", b.getName());
    }

    /**
     * Test for getList
     */
    @Test
    public void getListTest() {
        var b = new Board(SOME_USER, "b");
        assertEquals(b.getList(), new ArrayList<>());
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b1 = new Board(SOME_USER, cls, "cc");
        assertEquals(cls, b1.getList());
    }

    /**
     * Test for addList
     */
    @Test
    public void addFirstListTest() {
        var b = new Board(SOME_USER, "b");
        CardList cL1 = new CardList("cL1");
        b.addList(cL1);
        assertTrue(b.getList().contains(cL1));
    }

    /**
     * Test for addList
     */
    @Test
    public void addNotFirstListTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b = new Board(SOME_USER, cls, "cc");
        CardList cL3 = new CardList("cL3");
        b.addList(cL3);
        assertTrue(b.getList().contains(cL3));
    }

    /**
     * Test for addEmptyList
     */
    @Test
    public void addFirstEmptyListTest() {
        var b = new Board(SOME_USER, "b");
        b.addEmptyList();
        assertEquals(b.getList().size(), 1);
    }

    /**
     * Test for addEmptyList
     */
    @Test
    public void addNotFirstEmptyListTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b = new Board(SOME_USER, cls, "cc");
        b.addEmptyList();
        assertEquals(b.getList().size(), 3);
        assertEquals(b.getList().get(2), new CardList());
    }

    /**
     * Test for equals
     */
    @Test
    public void equalBoardsTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b = new Board(SOME_USER, cls, "cc");
        var b1 = new Board(SOME_USER, cls, "cc");
        assertEquals(b, b1);
    }

    /**
     * Test for equals
     */
    @Test
    public void notEqualBoardsTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        Board b = new Board(SOME_USER, cls, "cc");
        Board b1 = new Board(SOME_USER, cls, "cc");
        b1.setName("vv");
        assertNotEquals(b, b1);
    }

    /**
     * Test for hashCode
     */
    @Test
    public void equalHashCodesTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b = new Board(SOME_USER, cls, "cc");
        var b1 = new Board(SOME_USER, cls, "cc");
        assertEquals(b.hashCode(), b1.hashCode());
    }

    /**
     * Test for hashCode
     */
    @Test
    public void notEqualHashCodesTest() {
        CardList cL1 = new CardList("cL1");
        CardList cL2 = new CardList("cL2");
        List<CardList> cls = new ArrayList<>();
        cls.add(cL1);
        cls.add(cL2);
        var b = new Board(SOME_USER, cls, "cc");
        var b1 = new Board(SOME_USER, cls, "c");
        assertNotEquals(b.hashCode(), b1.hashCode());
    }

    /**
     * Test for hasUser
     */
    @Test
    public void hasUserTest() {
        SOME_USER.setId(1);
        Board b = new Board(SOME_USER, "b");
        assertTrue(b.hasUser(1));
        SOME_OTHER_USER.setId(22);
        assertFalse(b.hasUser(22));
    }
}
