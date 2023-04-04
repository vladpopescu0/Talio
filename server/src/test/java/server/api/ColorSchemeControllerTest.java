package server.api;

import commons.ColorScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ColorSchemeControllerTest {
    TestColorSchemeRepository repo;
    ColorSchemeController controller;
    private MessageChannel channel;

    SimpMessagingTemplate msg;

    /**
     * Sets up the testing environment
     */
    @BeforeEach
    public void setup() {
        channel = (message, timeout) -> true;
        msg = new SimpMessagingTemplate(channel);
        repo = new TestColorSchemeRepository();
        controller = new ColorSchemeController(repo,msg);
    }

    /**
     * test for add endpoint
     */
    @Test
    public void addColorSchemeTest(){
        ColorScheme c = new ColorScheme("red","green","black","yellow");
        c.setIdOnlyTest(5);
        var response = controller.add(c);
        assertEquals(response.getBody(),c);

    }
    /**
     * test for add endpoint
     */
    @Test
    public void addColorSchemeNullTest(){
        var response = controller.add(null);
        assertEquals(response.getStatusCode(),BAD_REQUEST);
    }
    /**
     * test for updateColorScheme
     */
    @Test
    public void updateColorSchemeTest(){
        ColorScheme c = new ColorScheme("red","green","black","yellow");
        controller.add(c);
        ColorScheme newColor = new ColorScheme();
        newColor.setIdOnlyTest(0);
        var response = controller.updateColorScheme(0,newColor);
        assertEquals(repo.find(newColor.getId()).get(),response.getBody());
        assertEquals("save", repo.calledMethods.get(0));
        assertEquals("existsById", repo.calledMethods.get(1));
        assertEquals("save", repo.calledMethods.get(2));
    }
    /**
     * test for updateColorScheme
     */
    @Test
    public void updateColorSchemeNullTest(){
        ColorScheme c = new ColorScheme("red","green","black","yellow");
        controller.add(c);
        var response = controller.updateColorScheme(0,null);
        assertEquals(400,response.getStatusCodeValue());
        assertEquals("save", repo.calledMethods.get(0));
        assertEquals("existsById", repo.calledMethods.get(1));
    }
}
