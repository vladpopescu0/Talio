package server.api;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UserControllerTest {
    private TestUserRepository repo;
    private UserController sut;

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        repo = new TestUserRepository();
        sut = new UserController(repo);
    }

    /**
     * Test for add
     */
    @Test
    public void addNullUserTest() {
        var actual = sut.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addNullOrEmptyUsernameTest() {
        var actual1 = sut.add(new User(null));
        var actual2 = sut.add(new User(""));
        assertEquals(BAD_REQUEST, actual1.getStatusCode());
        assertEquals(BAD_REQUEST, actual2.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addActualUserTest() {
        User u = new User("u");
        var actual = sut.add(u);
        assertEquals(actual.getBody(), u);
    }

    /**
     * Test for add
     */
    @Test
    public void databaseIsUsed() {
        sut.add(new User("u"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for getAll
     */
    @Test
    public void getAllTest() {
        User u1 = new User("a");
        User u2 = new User("b");
        User u3 = new User("c");
        sut.add(u1);
        sut.add(u2);
        sut.add(u3);
        List<User> users = sut.getAll();
        assertEquals(users.size(), 3);
        assertTrue(users.contains(u1));
        assertTrue(users.contains(u2));
        assertTrue(users.contains(u3));
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    /**
     * Test for getByID
     */
    @Test
    public void getByNegativeIdTest() {
        var actual = sut.getById(-2);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getByID
     */
    @Test
    public void getByTooLargeIdTest() {
        User u1 = new User("a");
        User u2 = new User("b");
        User u3 = new User("c");
        sut.add(u1);
        sut.add(u2);
        sut.add(u3);
        var actual = sut.getById(212);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getByID
     */
    @Test
    public void getByIdTest() {
        User u1 = new User("a");
        User u2 = new User("b");
        User u3 = new User("c");
        sut.add(u1);
        sut.add(u2);
        sut.add(u3);
        var actual1 = sut.getById(0);
        var actual2 = sut.getById(1);
        var actual3 = sut.getById(2);
        assertEquals(u1, actual1.getBody());
        assertEquals(u2, actual2.getBody());
        assertEquals(u3, actual3.getBody());
        assertTrue(repo.calledMethods.contains("findById"));
    }

    /**
     * Test for modifyName
     */
    @Test
    public void modifyNameNonExistingBoard() {
        var actual = sut.modifyName(332, "Bad");
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for modifyName
     */
    @Test
    public void modifyNameTest() {
        User u1 = new User("a");
        User u2 = new User("b");
        User u3 = new User("c");
        sut.add(u1);
        sut.add(u2);
        sut.add(u3);
        var actual = sut.modifyName(2, "cc");
        assertEquals(actual.getBody().getUsername(), "cc");
        //assertEquals(sut.getById(2).getBody().getName(), "cc");
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for getByUsername
     */
    @Test
    public void getByUsernameTest() {
        User u1 = new User("a");
        User u2 = new User("b");
        User u3 = new User("a");
        sut.add(u1);
        sut.add(u2);
        sut.add(u3);
        var actual1 = sut.getByUsername("cc");
        assertEquals(new ArrayList<>(), actual1.getBody());
        var actual2 = sut.getByUsername("b");
        assertEquals(actual2.getBody().size(), 1);
        assertTrue(actual2.getBody().contains(u2));
        var actual3 = sut.getByUsername("a");
        assertEquals(actual3.getBody().size(), 2);
        assertTrue(actual3.getBody().contains(u1));
        assertTrue(actual3.getBody().contains(u3));
    }
}