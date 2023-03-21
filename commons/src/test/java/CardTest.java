import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.Test;
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

    @Test
    public void getParentListTest() {
        Card c = new Card("a");
        CardList cl = new CardList("aa");
        c.setParentCardList(cl);
        assertEquals(c.getParentCardList(), cl);
    }
}
