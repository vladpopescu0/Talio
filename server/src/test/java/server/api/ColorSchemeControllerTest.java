package server.api;

import commons.ColorScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ColorSchemeControllerTest {
    TestColorSchemeRepository repo;
    ColorSchemeController controller;

    /**
     * Sets up the testing environment
     */
    @BeforeEach
    public void setup() {
        repo = new TestColorSchemeRepository();
        controller = new ColorSchemeController(repo);
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

}
