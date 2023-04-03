package server.api;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CardControllerTest {
    private TestCardRepository repo;
    private TestCardListRepository cardListRepository;
    private TestTaskRepository taskRepository;
    private TestTagRepository tagRepository;
    private CardController sut;
    private TagController tagSut;
    private SimpMessagingTemplate msg;
    private MessageChannel channel;
    private TaskController tSut;

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        channel = (message, timeout) -> true;
        msg = new SimpMessagingTemplate(channel);
        repo = new TestCardRepository();
        taskRepository = new TestTaskRepository();
        cardListRepository = new TestCardListRepository();
        tagRepository = new TestTagRepository();
        sut = new CardController(repo, cardListRepository,msg, taskRepository, tagRepository);
        tSut = new TaskController(taskRepository);
        tagSut = new TagController(tagRepository, repo);
    }

    /**
     * Test for add
     */
    @Test
    public void addNullCardTest() {
        var actual = sut.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addNullOrEmptyNameCardTest() {
        var actual = sut.add(new Card(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        var empty = sut.add(new Card(""));
        assertEquals(BAD_REQUEST, empty.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addActualCardTest() {
        Card c = new Card("c");
        var actual = sut.add(c);
        assertEquals(c, actual.getBody());
    }

    /**
     * Test for add
     */
    @Test
    public void databaseIsUsed() {
        sut.add(new Card("c"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for getALl
     */
    @Test
    public void getAllTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        List<Card> cards = sut.getAll();
        assertEquals(cards.size(), 3);
        assertTrue(cards.contains(c1));
        assertTrue(cards.contains(c2));
        assertTrue(cards.contains(c3));
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
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var actual = sut.getById(22);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getById
     */
    @Test
    public void getByIdTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var actual1 = sut.getById(0);
        var actual2 = sut.getById(1);
        var actual3 = sut.getById(2);
        assertEquals(actual1.getBody(), c1);
        assertEquals(actual2.getBody(), c2);
        assertEquals(actual3.getBody(), c3);
        assertTrue(repo.calledMethods.contains("findById"));
    }

    /**
     * Test for modifyName
     */
    @Test
    public void modifyNameNonExistingCardTest() {
        var actual = sut.modifyName(222, "Bad");
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for modifyName
     */
    @Test
    public void modifyNameTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var actual = sut.modifyName(2, "cc");
        assertEquals(actual.getBody().getName(), "cc");
        var actual2 = sut.getById(2);
        assertEquals(actual2.getBody().getName(), "cc");
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for removeCard
     */
    @Test
    public void removeNonExistingCard() {
        var alert = sut.removeCard(22);
        assertEquals(BAD_REQUEST, alert.getStatusCode());
    }

    /**
     * Test for removeCard
     */
    @Test
    public void removeCardTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var alert = sut.removeCard(2);
        assertEquals(alert.getBody(), c3);
        assertEquals(repo.cards.size(), 2);
        assertTrue(repo.calledMethods.contains("deleteById"));
    }

    /**
     * Test for updateCardDetails
     */
    @Test
    public void updateCardDetailsTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        Card c4 = sut.getById(2).getBody();
        c4.setDescription("99");
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        t1.setId(-1);
        t2.setId(-2);
        t3.setId(-3);
        tSut.add(t1);
        tSut.add(t2);
        tSut.add(t3);
        List<Task> tasks = new ArrayList<>();
        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);
        Card c5 = sut.getById(1).getBody();
        c5.setTasks(tasks);
        var actual1 = sut.updateCardDetails(2, c4);
        assertEquals(actual1.getBody().getDescription(), "99");
        var actual2 = sut.updateCardDetails(1, c5);
        assertEquals(actual2.getBody().getTasks(), tasks);
    }

    /**
     * Test for deleteTaskFromCard
     */
    @Test
    public void deleteTaskFromNonExistingCardTest() {
        var actual = sut.deleteTaskFromCard(22, 4);
        assertEquals(actual.getStatusCode(), NOT_FOUND);
    }

    /**
     * Test for deleteTaskFromCard
     */
    @Test
    public void deleteTaskFromCardTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        Task t1 = new Task("1");
        Task t2 = new Task("2");
        Task t3 = new Task("3");
        t1.setId(-1);
        t2.setId(-2);
        t3.setId(-3);
        tSut.add(t1);
        tSut.add(t2);
        tSut.add(t3);
        List<Task> tasks = new ArrayList<>();
        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);
        c3.setTasks(tasks);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var actual = sut.deleteTaskFromCard(2, 2);
        List<Task> withoutT3 = new ArrayList<>();
        withoutT3.add(t1);
        withoutT3.add(t2);
        assertEquals(actual.getBody().getTasks(), withoutT3);
    }

    /**
     * Test for addTaskToCard
     */
    @Test
    public void addBadTaskToCardTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var actual1 = sut.addTaskToCard(2, null);
        assertEquals(actual1.getStatusCode(), BAD_REQUEST);
        var actual2 = sut.addTaskToCard(2, new Task());
        assertEquals(actual2.getStatusCode(), BAD_REQUEST);
        var actual3 = sut.addTaskToCard(2, new Task(""));
        assertEquals(actual3.getStatusCode(), BAD_REQUEST);
    }

    /**
     * Test for addTaskToCard
     */
    @Test
    public void addTaskToNonExistingCardTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        var actual = sut.addTaskToCard(222, new Task("5"));
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
    }

    /**
     * Test for addTaskToCard
     */
    @Test
    public void addTaskToCardTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        Card c3 = new Card("3");
        c1.setId(-1);
        c2.setId(-2);
        c3.setId(-3);
        sut.add(c1);
        sut.add(c2);
        sut.add(c3);
        Task t = new Task("5");
        List<Task> tasks = new ArrayList<>();
        tasks.add(t);
        tSut.add(t);
        sut.addTaskToCard(2, t);
        Card c5 = sut.getById(2).getBody();
        assertEquals(c5.getTasks(), tasks);
    }

    /**
     * Test for addTags with wrong arguments
     */
    @Test
    public void testAddTagsNonExisting() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        tag1.setId(1);
        tag2.setId(2);
        tag3.setId(3);
        var actual = sut.addTags(22, List.of(tag1, tag2, tag3));
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
        Card c = new Card("card");
        c.setTags(new ArrayList<>());
        c.setId(5);
        sut.add(c);
        actual = sut.addTags(c.getId(), new ArrayList<>());
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
        actual = sut.addTags(c.getId(), List.of(tag1, tag2, tag3));
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
    }

    /**
     * Test for addTags with correct arguments
     */
    @Test
    public void testAddTags() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        tag1.setId(1);
        tag2.setId(2);
        tag3.setId(3);
        tagSut.add(tag1);
        tagSut.add(tag2);
        tagSut.add(tag3);
        Card c = new Card("card");
        c.setTags(new ArrayList<>());
        c.setId(5);
        sut.add(c);
        var actual = sut.addTags(c.getId(), List.of(tag1, tag2, tag3));
        assertEquals(actual.getBody(), repo.getById(c.getId()));
    }

    /**
     * Test for removeTag with wrong arguments
     */
    @Test
    public void testRemoveTagNonExisting() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        tag1.setId(1);
        tag2.setId(2);
        tag3.setId(3);
        var actual = sut.removeTag(22, tag1);
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
        Card c = new Card("card");
        c.setTags(new ArrayList<>());
        c.setId(5);
        sut.add(c);
        actual = sut.removeTag(c.getId(), null);
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
        actual = sut.removeTag(c.getId(), tag1);
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
    }

    /**
     * Test for removeTag with correct arguments
     */
    @Test
    public void testRemoveTag() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        tag1.setId(1);
        tag2.setId(2);
        tag3.setId(3);
        tagSut.add(tag1);
        tagSut.add(tag2);
        tagSut.add(tag3);
        Card c = new Card("card");
        c.setTags(new ArrayList<>());
        c.setId(5);
        sut.add(c);
        var actual = sut.removeTag(c.getId(), tag2);
        assertEquals(actual.getBody(), repo.getById(c.getId()));
    }
}
