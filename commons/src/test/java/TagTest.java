import commons.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    /**
     * Test for Constructor
     */
    @Test
    public void constructorTest() {
        Tag t = new Tag("tag");
        assertNotNull(t);
        assertEquals(t.getName(), "tag");
        Tag t1 = new Tag("tag", "red");
        assertEquals(t1.getName(), "tag");
        assertEquals(t1.getColor(), "red");
    }

    /**
     * Test for getName
     */
    @Test
    public void getNameTest() {
        Tag t = new Tag("tag");
        assertEquals("tag", t.getName());
    }

    /**
     * Test for setUsername
     */
    @Test
    public void setNameTest() {
        Tag t = new Tag("tag");
        t.setName("tag1");
        assertEquals("tag1", t.getName());
    }

    /**
     * Test for equals
     */
    @Test
    public void equalsTagsTest() {
        Tag t = new Tag("tag");
        Tag t1 = new Tag("tag");
        assertEquals(t, t1);
    }

    /**
     * Test for equals
     */
    @Test
    public void notEqualsTagsTest() {
        Tag t = new Tag("tag");
        Tag t1 = new Tag("tag1");
        assertNotEquals(t, t1);
    }

    /**
     * Test for hashCode
     */
    @Test
    public void equalHashCodes() {
        Tag t = new Tag("tag");
        Tag t1 = new Tag("tag");
        assertEquals(t.hashCode(), t1.hashCode());
    }

    /**
     * Test for hashCode
     */
    @Test
    public void notEqualHashCode() {
        Tag t = new Tag("tag");
        Tag t1 = new Tag("tag1");
        assertNotEquals(t.hashCode(), t1.hashCode());
    }

    @Test
    public void getColorTest() {
        Tag t = new Tag();
        assertNull(t.getColor());
    }

    @Test
    public void setColorTest() {
        Tag t = new Tag("t");
        t.setColor("red");
        assertEquals(t.getColor(), "red");
    }
}
