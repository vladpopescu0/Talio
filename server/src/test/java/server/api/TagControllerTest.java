package server.api;

import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TagControllerTest {
    private TestTagRepository repo;
    private TestCardRepository cardRepo;
    private TestCardListRepository clRepo;
    private TestTaskRepository taskRepo;
    private TagController controller;
    private CardController cardController;
    private SimpMessagingTemplate msg;
    private MessageChannel channel;

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        channel = (message, timeout) -> true;
        msg = new SimpMessagingTemplate(channel);
        repo = new TestTagRepository();
        cardRepo = new TestCardRepository();
        clRepo = new TestCardListRepository();
        taskRepo = new TestTaskRepository();
        controller = new TagController(repo, cardRepo, msg);
        cardController = new CardController(cardRepo, clRepo, msg, taskRepo, repo);
    }

    /**
     * Test for add
     */
    @Test
    public void addNullTagTest() {
        var actual = controller.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addNullOrEmptyNameTest() {
        var actual1 = controller.add(new Tag(null));
        var actual2 = controller.add(new Tag(""));
        assertEquals(BAD_REQUEST, actual1.getStatusCode());
        assertEquals(BAD_REQUEST, actual2.getStatusCode());
    }

    /**
     * Test for add
     */
    @Test
    public void addActualTagTest() {
        Tag tag = new Tag("tag");
        var actual = controller.add(tag);
        assertEquals(actual.getBody(), tag);
    }

    /**
     * Test for add
     */
    @Test
    public void databaseIsUsed() {
        controller.add(new Tag("tag"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for getAll
     */
    @Test
    public void getAllTest() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        controller.add(tag1);
        controller.add(tag2);
        controller.add(tag3);
        List<Tag> tags = controller.getAll();
        assertEquals(tags.size(), 3);
        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
        assertTrue(tags.contains(tag3));
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    /**
     * Test for getByID
     */
    @Test
    public void getByNegativeIdTest() {
        var actual = controller.getById(-2);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getByID
     */
    @Test
    public void getByTooLargeIdTest() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        controller.add(tag1);
        controller.add(tag2);
        controller.add(tag3);
        var actual = controller.getById(212);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    /**
     * Test for getByID
     */
    @Test
    public void getByIdTest() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        controller.add(tag1);
        controller.add(tag2);
        controller.add(tag3);
        var actual1 = controller.getById(0);
        var actual2 = controller.getById(1);
        var actual3 = controller.getById(2);
        assertEquals(tag1, actual1.getBody());
        assertEquals(tag2, actual2.getBody());
        assertEquals(tag3, actual3.getBody());
        assertTrue(repo.calledMethods.contains("findById"));
    }

    /**
     * Test for modifyTag
     */
    @Test
    public void modifyTagTest() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        controller.add(tag1);
        controller.add(tag2);
        controller.add(tag3);
        var actual = controller.modifyTag(2, new Tag("tag"));
        assertEquals(actual.getBody().getName(), "tag");
        assertTrue(repo.calledMethods.contains("save"));
    }

    /**
     * Test for modifyTag, with a non-existing Tag
     */
    @Test
    public void modifyNonExistingTagTest() {
        Tag t = new Tag("22");
        var actual = controller.modifyTag(22, t);
        assertEquals(actual.getStatusCode(), BAD_REQUEST);
    }

    /**
     * Test for removeTag
     */
    @Test
    public void removeTagTest() {
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Tag tag3 = new Tag("tag 3");
        controller.add(tag1);
        controller.add(tag2);
        controller.add(tag3);
        Card c = new Card("Card");
        c.setTags(new ArrayList<>());
        c.setId(1);
        cardController.add(c);
        cardController.addTags(c.getId(), List.of(tag1, tag2, tag3));
        controller.removeTag(1);
        c = cardController.getById(c.getId()).getBody();
        assertTrue(c.getTags().contains(tag1));
        assertTrue(c.getTags().contains(tag3));
        assertFalse(c.getTags().contains(tag2));
    }

    /**
     * Test for removeTag, when the Tag is non-existing
     */
    @Test
    public void removeNonExistingTagTest() {
        var actual = controller.removeTag(22);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
}