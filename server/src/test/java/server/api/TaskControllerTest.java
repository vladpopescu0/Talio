package server.api;

import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TaskControllerTest {
    private TestTaskRepository repo;
    private TaskController sut;

    /**
     * Setup
     */
    @BeforeEach
    public void setUp() {
        repo = new TestTaskRepository();
        sut = new TaskController(repo);
    }

    /**
     * Test for add
     */
    @Test
    public void addNullTaskTest() {
        var actual = sut.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addNullOrEmptyTitleTaskTest() {
        var actual = sut.add(new Task(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        var empty = sut.add(new Task(""));
        assertEquals(BAD_REQUEST, empty.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addActualTaskTest() {
        Task t = new Task("c");
        var actual = sut.add(t);
        assertEquals(t, actual.getBody());
    }

    /**
     * Test for add
     */
    @Test
    public void databaseIsUsed() {
        sut.add(new Task("c"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for getALl
     */
    @Test
    public void getAllTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        t1.setId(-1);
        t2.setId(-2);
        t3.setId(-3);
        sut.add(t1);
        sut.add(t2);
        sut.add(t3);
        List<Task> tasks = sut.getAll();
        assertEquals(tasks.size(), 3);
        assertTrue(tasks.contains(t1));
        assertTrue(tasks.contains(t2));
        assertTrue(tasks.contains(t3));
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
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        sut.add(t1);
        sut.add(t2);
        sut.add(t3);
        var actual = sut.getById(22);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getById
     */
    @Test
    public void getByIDTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        t1.setId(-1);
        t2.setId(-2);
        t3.setId(-3);
        sut.add(t1);
        sut.add(t2);
        sut.add(t3);
        var actual1 = sut.getById(0);
        var actual2 = sut.getById(1);
        var actual3 = sut.getById(2);
        assertEquals(actual1.getBody(), t1);
        assertEquals(actual2.getBody(), t2);
        assertEquals(actual3.getBody(), t3);
        assertTrue(repo.calledMethods.contains("findById"));
    }

    /**
     * Test for updateTask
     */
    @Test
    public void updateNonExistingCardTest() {
        var actual = sut.updateTask(222, new Task());
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for updateTask
     */
    @Test
    public void updateTaskTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        t1.setId(-1);
        t2.setId(-2);
        t3.setId(-3);
        sut.add(t1);
        sut.add(t2);
        sut.add(t3);
        Task t4 = sut.getById(1).getBody();
        t4.changeStatus();
        Task t5 = sut.getById(2).getBody();
        t5.setTitle("4");
        var actual1 = sut.updateTask(1, t4);
        var actual2 = sut.updateTask(2, t5);
        assertEquals(actual1.getBody().getStatus(), true);
        assertEquals(actual2.getBody().getTitle(), "4");
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for removeTask
     */
    @Test
    public void removeNonExistingTaskTest() {
        var alert = sut.removeTask(22);
        assertEquals(BAD_REQUEST, alert.getStatusCode());
    }

    /**
     * Test for removeTask
     */
    @Test
    public void removeTaskTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        t1.setId(-1);
        t2.setId(-2);
        t3.setId(-3);
        sut.add(t1);
        sut.add(t2);
        sut.add(t3);
        var alert = sut.removeTask(2);
        assertEquals(alert.getBody(), t3);
        assertEquals(repo.tasks.size(), 2);
        assertTrue(repo.calledMethods.contains("deleteById"));
    }
}
