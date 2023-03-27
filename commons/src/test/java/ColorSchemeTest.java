import commons.ColorScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColorSchemeTest {
    ColorScheme colorSchemeDefault = new ColorScheme();

    @Test
    public void emptyConstructorTest_BGLightGetter(){
        assertEquals(colorSchemeDefault.getColorBGlight(),"#FFFFFF");
    }

    @Test
    public void emptyConstructorTest_BGDarkGetter(){
        assertEquals(colorSchemeDefault.getColorBGdark(),"#000000");
    }

    @Test
    public void emptyConstructorTest_colorFontGetter(){
        assertEquals(colorSchemeDefault.getColorFont(),"#000000");
    }
    @Test
    public void emptyConstructorTest_colorLighterGetter(){
        assertEquals(colorSchemeDefault.getColorLighter(),"#808080");
    }
    @Test
    public void fourParamConstructor_BGLightGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorBGlight(),"red");
    }
    @Test
    public void fourParamConstructor_BGDarkGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorBGdark(),"green");
    }
    @Test
    public void fourParamConstructor_colorLighterGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorLighter(),"blue");
    }
    @Test
    public void fourParamConstructor_colorFontGetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorScheme.getColorFont(),"yellow");
    }
    @Test
    public void fourParamConstructor_colorFontSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorFont("purple");
        assertEquals(colorScheme.getColorFont(),"purple");
    }
    @Test
    public void fourParamConstructor_colorLighterSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorLighter("purple");
        assertEquals(colorScheme.getColorLighter(),"purple");
    }
    @Test
    public void fourParamConstructor_colorBGlightSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorBGlight("purple");
        assertEquals(colorScheme.getColorBGlight(),"purple");
    }
    @Test
    public void fourParamConstructor_colorBGdarkSetter(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        colorScheme.setColorBGdark("purple");
        assertEquals(colorScheme.getColorBGdark(),"purple");
    }
    @Test
    public void fourParamConstructor_isEqualTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeCopy = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorSchemeCopy,colorScheme);
    }
    @Test
    public void fourParamConstructor_notEqualTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeWrongCopy = new ColorScheme("white","green","blue","yellow");
        assertNotEquals(colorSchemeWrongCopy,colorScheme);
    }
    @Test
    public void fourParamConstructor_sameHashTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeCopy = new ColorScheme("red","green","blue","yellow");
        assertEquals(colorSchemeCopy.hashCode(),colorScheme.hashCode());
    }
    @Test
    public void fourParamConstructor_notSameHashTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        ColorScheme colorSchemeCopy = new ColorScheme("blue","yellow","green","red");
        assertNotEquals(colorSchemeCopy.hashCode(),colorScheme.hashCode());
    }
    @Test
    public void fourParamConstructor_toStringTest(){
        ColorScheme colorScheme = new ColorScheme("red","green","blue","yellow");
        assertTrue(colorScheme.toString().contains("[\n" +
                "  colorBGdark=green\n" +
                "  colorBGlight=red\n" +
                "  colorFont=yellow\n" +
                "  colorLighter=blue\n" +
                "]"));
    }

}
