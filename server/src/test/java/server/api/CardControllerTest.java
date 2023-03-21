package server.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {
    private TestCardRepository repo;
    private TestCardListRepository cardListRepository;
    private CardController sut;

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        repo = new TestCardRepository();
        cardListRepository = new TestCardListRepository();
        sut = new CardController(repo, cardListRepository);
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
}
