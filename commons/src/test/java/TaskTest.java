import commons.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    /**
     * Test for getTitle
     */
    @Test
    public void getTitleTest() {
        Task t = new Task("title");
        assertEquals("title", t.getTitle());
    }

    /**
     * Test for setTitle
     */
    @Test
    public void setTitleTest() {
        Task t =  new Task();
        t.setTitle("title1");
        assertEquals(t.getTitle(), "title1");
    }

    /**
     * Test for getID
     */
    @Test
    public void getIdTest() {
        Task t = new Task("task");
        assertEquals(t.getId(), 0);
    }

    /**
     * Test for setID
     */
    @Test
    public void setIdTest() {
        Task t = new Task("t");
        t.setId(222);
        assertEquals(222, t.getId());
    }

    /**
     * Test for getStatus
     */
    @Test
    public void getStatusTest() {
        Task t = new Task("t");
        assertFalse(t.getStatus());
    }

    /**
     * Test for changeStatus
     */
    @Test
    public void changeStatusTest() {
        Task t = new Task("t");
        t.changeStatus();
        assertTrue(t.getStatus());
        t.changeStatus();
        assertFalse(t.getStatus());
    }

    /**
     * test for equals
     */
    @Test
    public void equalTasksTest() {
        Task t = new Task("2");
        Task t1 = new Task("2");
        assertEquals(t, t1);
    }

    /**
     * Test for equals
     */
    @Test
    public void notEqualTasksTest() {
        Task t = new Task("2");
        Task t1 = new Task("21");
        assertNotEquals(t, t1);
    }

    /**
     * Test for hashCode
     */
    @Test
    public void equalHashCodeTest() {
        Task t = new Task("2");
        Task t1 = new Task("2");
        assertEquals(t.hashCode(), t1.hashCode());
    }

    /**
     * Test for hashCode
     */
    @Test
    public void notEqualHashCodeTest() {
        Task t = new Task("2");
        Task t1 = new Task("21");
        assertNotEquals(t.hashCode(), t1.hashCode());
    }
}
