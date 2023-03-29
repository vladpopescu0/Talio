import commons.Card;
import commons.CardList;
import commons.Tag;
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
                new ArrayList<>(), new ArrayList<>(), "aa");
        assertEquals(c.getDescription(), "aa");
    }

    /**
     * Test for setDescription
     */
    @Test
    public void setDescriptionTest() {
        Card c = new Card("Card", new CardList(),
                new ArrayList<>(), new ArrayList<>(), "desc");
        assertEquals(c.getDescription(), "desc");
    }

    /**
     * Test for hasDescription
     */
    @Test
    public void hasDescriptionTest() {
        Card c = new Card("Card", new CardList(),
                new ArrayList<>(), new ArrayList<>(), "desc");
        assertTrue(c.hasDescription());
        Card c1 = new Card("Card", new CardList(), new ArrayList<>(),
                new ArrayList<>(), "");
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
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(), list, tagList, "a");
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
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(), list, tagList, "a");
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
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(),
                new ArrayList<>(), tagList, "a");
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
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("c", new CardList(),
                new ArrayList<>(), tagList, "desc");
        assertEquals("", c.tasksLabel());
        c.setTasks(list);
        assertEquals(c.tasksLabel(), "(0/3 done)");
        t1.changeStatus();
        assertEquals(c.tasksLabel(), "(1/3 done)");
        Card c1 = new Card("a");
        assertEquals(c1.tasksLabel(), "");
    }

    /**
     * Test for getTags
     */
    @Test
    public void getTagsTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(), list, tagList, "a");
        assertEquals(tagList, c.getTags());
    }

    /**
     * Test for setTags
     */
    @Test
    public void setTagsTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(),
                list, new ArrayList<>(), "a");
        c.setTags(tagList);
        assertEquals(c.getTags(), tagList);
    }

    /**
     * Test for addTag
     */
    @Test
    public void addTagTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(), list, tagList, "a");
        Tag tag4 = new Tag("tag4");
        c.addTag(tag4);
        List<Tag> newList = new ArrayList<>();
        newList.add(tag1);
        newList.add(tag2);
        newList.add(tag3);
        newList.add(tag4);
        assertEquals(newList, c.getTags());
    }

    /**
     * Test for removeTag
     */
    @Test
    public void removeTagTest() {
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        List<Task> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        Tag tag1 = new Tag("tag1");
        tag1.setId(1);
        Tag tag2 = new Tag("tag2");
        tag2.setId(2);
        Tag tag3 = new Tag("tag3");
        tag3.setId(3);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        Card c = new Card("a", new CardList(), list, tagList, "a");
        c.removeTag(tag2);
        List<Tag> newList = new ArrayList<>();
        newList.add(tag1);
        newList.add(tag3);
        assertEquals(newList, c.getTags());
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
