import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CardListTest {

    /**
     * Test for constructor
     */
    @Test
    public void noListConstructorTest() {
        CardList cL = new CardList("a");
        assertEquals(cL.getName(), "a");
        assertEquals(cL.getCards(), new ArrayList<>());
    }

    /**
     * Test for constructor
     */
    @Test
    public void withListConstructorTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        assertEquals(cL.getName(), "a");
        assertEquals(cL.getCards(), l);
    }

    /**
     * Test for constructor
     */
    @Test
    public void idConstructorTest() {
        CardList c = new CardList("a", 22);
        assertEquals("a", c.getName());
        assertEquals(22, c.getId());
    }

    /**
     * Test for setId
     */
    @Test
    public void setIdTest() {
        CardList c = new CardList("a");
        c.setId((long) 222);
        assertEquals(222, c.getId());
    }

    /**
     * Test for getName
     */
    @Test
    public void getNameTest() {
        CardList cL = new CardList("b");
        assertEquals("b", cL.getName());
    }

    /**
     * Test for setName
     */
    @Test
    public void setNameTest() {
        CardList cL = new CardList("b");
        cL.setName("B");
        assertEquals("B", cL.getName());
    }

    /**
     * Test for getCards
     */
    @Test
    public void getCardsTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        assertEquals(cL.getCards(), l);
    }

    /**
     * Test for addCard
     */
    @Test
    public void addCardToEmptyCardListTest() {
        CardList cL = new CardList("b");
        Card c1 = new Card("c");
        boolean check = cL.addCard(c1);
        assertEquals(cL.getCards().size(), 1);
        assertTrue(cL.getCards().contains(c1));
        assertTrue(check);
        check = cL.addCard(null);
        assertEquals(cL.getCards().size(), 1);
        assertFalse(check);
    }

    /**
     * Test for addCard
     */
    @Test
    public void addCardToNonEmptyCardListTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        Card c3 = new Card("c");
        boolean check = cL.addCard(c3);
        assertEquals(cL.getCards().size(), 3);
        assertTrue(cL.getCards().contains(c3));
        assertTrue(check);
        check = cL.addCard(null);
        assertEquals(cL.getCards().size(), 3);
        assertFalse(check);
    }

    /**
     * Test for equals
     */
    @Test
    public void equalCardListsTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        CardList cL1 = new CardList("a", l);
        assertEquals(cL, cL1);
    }

    /**
     * Test for equals
     */
    @Test
    public void notEqualCardListsTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        CardList cL1 = new CardList("a");
        CardList cL2 = new CardList("b", l);
        assertNotEquals(cL, cL1);
        assertNotEquals(cL, cL2);
        assertNotEquals(cL1, cL2);
    }

    /**
     * Test for hashcode
     */
    @Test
    public void equalHashCodeTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        CardList cL1 = new CardList("a", l);
        assertEquals(cL.hashCode(), cL1.hashCode());
    }

    /**
     * Test for hashCode
     */
    @Test
    public void notEqualHashCodeTest() {
        Card c1 = new Card("1");
        Card c2 = new Card("2");
        List<Card> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        CardList cL = new CardList("a", l);
        CardList cL1 = new CardList("a");
        CardList cL2 = new CardList("b", l);
        assertNotEquals(cL.hashCode(), cL1.hashCode());
        assertNotEquals(cL.hashCode(), cL2.hashCode());
        assertNotEquals(cL1.hashCode(), cL2.hashCode());
    }

}
