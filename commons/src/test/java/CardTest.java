import commons.Card;
import commons.CardList;
import commons.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    /**
     * Test for constructor
     */
    @Test
    public void constructorTest() {
        Card c = new Card("c");
        assertEquals("c", c.getName());
    }

    /**
     * Test for getName
     */
    @Test
    public void getNameTest() {
        Card c = new Card("c");
        assertEquals("c", c.getName());
    }

    /**
     * Test for setName
     */
    @Test
    public void setNameTest() {
        Card c = new Card("c");
        String s = c.setName("C");
        assertEquals(s, "C");
        assertEquals(c.getName(), "C");
    }

    /**
     * Test for getDescription
     */
    @Test
    public void getDescriptionTest() {
        Card c = new Card("Card", new CardList(),
                new ArrayList<>(), "aa");
        assertEquals(c.getDescription(), "aa");
    }

    /**
     * Test for setDescription
     */
    @Test
    public void setDescriptionTest() {
        Card c = new Card("Card", new CardList(),
                new ArrayList<>(), "desc");
        assertEquals(c.getDescription(), "desc");
    }

    /**
     * Test for hasDescription
     */
    @Test
    public void hasDescriptionTest() {
        Card c = new Card("Card", new CardList(),
                new ArrayList<>(), "desc");
        assertTrue(c.hasDescription());
        Card c1 = new Card("Card", new CardList(), new ArrayList<>(),
                "");
        assertFalse(c1.hasDescription());
        c1.setDescription(null);
        assertFalse(c1.hasDescription());
    }

    /**
     * Test for getTasks
     */
    @Test
    public void getTasksTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Card c = new Card("a", new CardList(), list, "a");
        assertEquals(list, c.getTasks());
    }

    /**
     * Test for addTask
     */
    @Test
    public void addTaskTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Card c = new Card("a", new CardList(), list, "a");
        Task t4 = new Task("4");
        c.addTask(t4);
        List<Task> newList = new ArrayList<>();
        newList.add(t1);
        newList.add(t2);
        newList.add(t3);
        newList.add(t4);
        assertEquals(newList, c.getTasks());
    }

    /**
     * Test for setTasks
     */
    @Test
    public void setTasksTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Card c = new Card("a", new CardList(),
                new ArrayList<>(), "a");
        c.setTasks(list);
        assertEquals(c.getTasks(), list);
    }

    /**
     * Test for taskLabel
     */
    @Test
    public void taskLabelTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Card c = new Card("c", new CardList(),
                new ArrayList<>(), "desc");
        assertEquals("", c.tasksLabel());
        c.setTasks(list);
        assertEquals(c.tasksLabel(), "(0/3 done)");
        t1.changeStatus();
        assertEquals(c.tasksLabel(), "(1/3 done)");
        Card c1 = new Card("a");
        assertEquals(c1.tasksLabel(), "");
    }

    /**
     * Test for equals
     */
    @Test
    public void equalCardsTest() {
        Card c = new Card("c");
        Card c1 = new Card("c");
        assertEquals(c, c1);
    }

    /**
     * Test for equals
     */
    @Test
    public void notEqualCardsTest() {
        Card c = new Card("c");
        Card c1 = new Card("C");
        assertNotEquals(c, c1);
    }

    /**
     * Test for hashCode
     */
    @Test
    public void equalHashCodeTest() {
        Card c = new Card("c");
        Card c1 = new Card("c");
        assertEquals(c.hashCode(), c1.hashCode());
    }

    /**
     * Test for hashCode
     */
    @Test
    public void notHashCodeTest() {
        Card c = new Card("c");
        Card c1 = new Card("C");
        assertNotEquals(c.hashCode(), c1.hashCode());
    }

    /**
     * Test for getParentList
     */
    @Test
    public void getParentListTest() {
        Card c = new Card("a");
        CardList cl = new CardList("aa");
        c.setParentCardList(cl);
        assertEquals(c.getParentCardList(), cl);
    }
}
