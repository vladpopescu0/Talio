import commons.ColorScheme;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ColorSchemeTest {
    ColorScheme colorSchemeDefault = new ColorScheme();

    /**
     * test for getter
     */
    @Test
    public void emptyConstructorTest_BGLightGetter(){
        assertEquals(colorSchemeDefault.getColorBGlight(),"#000000");
    }

    /**
     * test for getter
     */
    @Test
    public void emptyConstructorTest_BGDarkGetter(){
        assertEquals(colorSchemeDefault.getColorBGdark(),"#000000");
    }

    /**
     * test for getter
     */
    @Test
    public void emptyConstructorTest_colorFontGetter(){
        assertEquals(colorSchemeDefault.getColorFont(),"#FFFFFF");
    }

    /**
     * test for getter
     */
    @Test
    public void emptyConstructorTest_colorLighterGetter(){
        assertEquals(colorSchemeDefault.getColorLighter(),"#121212");
    }

    /**
     * test for getter
     */
    @Test
    public void fourParamConstructor_BGLightGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorBGlight(),"red");
    }

    /**
     * test for getter
     */
    @Test
    public void fourParamConstructor_BGDarkGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorBGdark(),"green");
    }

    /**
     * test for getter
     */
    @Test
    public void fourParamConstructor_colorLighterGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorLighter(),"blue");
    }

    /**
     * test for getter
     */
    @Test
    public void fourParamConstructor_colorFontGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorFont(),"yellow");
    }

    /**
     * test for setter
     */
    @Test
    public void fourParamConstructor_colorFontSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorFont("purple");
        assertEquals(colorScheme.getColorFont(),"purple");
    }

    /**
     * test for setter
     */
    @Test
    public void fourParamConstructor_colorLighterSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorLighter("purple");
        assertEquals(colorScheme.getColorLighter(),"purple");
    }

    /**
     * test for setter
     */
    @Test
    public void fourParamConstructor_colorBGlightSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorBGlight("purple");
        assertEquals(colorScheme.getColorBGlight(),"purple");
    }

    /**
     * test for setter
     */
    @Test
    public void fourParamConstructor_colorBGdarkSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorBGdark("purple");
        assertEquals(colorScheme.getColorBGdark(),"purple");
    }

    /**
     * test for equal
     */
    @Test
    public void fourParamConstructor_isEqualTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeCopy = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorSchemeCopy,colorScheme);
    }

    /**
     * test for equal
     */
    @Test
    public void fourParamConstructor_notEqualTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeWrongCopy = new ColorScheme("white","green","blue","yellow");
        assertNotEquals(colorSchemeWrongCopy,colorScheme);
    }

    /**
     * test for hash
     */
    @Test
    public void fourParamConstructor_sameHashTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeCopy = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorSchemeCopy.hashCode(),colorScheme.hashCode());
    }

    /**
     * test for hash
     */
    @Test
    public void fourParamConstructor_notSameHashTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeCopy = new ColorScheme("blue","yellow","green","red");
        assertNotEquals(colorSchemeCopy.hashCode(),colorScheme.hashCode());
    }

    /**
     * test for toString
     */
    @Test
    public void fourParamConstructor_toStringTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertTrue(colorScheme.toString().contains(
                "  colorBGdark=green\n" +
                "  colorBGlight=red\n" +
                "  colorFont=yellow\n" +
                "  colorLighter=blue\n"));
    }
    /**
     * test for setId, relevant only for backend tests
     */
    @Test
    public void setIdTest(){
        ColorScheme test = new ColorScheme();
        test.setId(890987);
        assertNotNull(test.getId());
    }
    /**
     * test for getId, rarely relevant for client only objects
     */
    @Test
    public void getIdTest(){
        ColorScheme test = new ColorScheme();
        test.setId(7);
        assertEquals(test.getId(),7);
    }
}
