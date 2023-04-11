package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChangeServerCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private ChangeServerCtrl sut;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        sut = new ChangeServerCtrl(mainCtrlMock, serverUtilsMock);
    }

    /**
     * Test for constructor
     */
    @Test
    public void constructorTest() {
        sut = new ChangeServerCtrl(mainCtrlMock, serverUtilsMock);
        assertNotNull(sut);
    }

    /**
     * Test for additionalHelp
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(), "Change Server specific shortcuts:\n"
                + "Enter - Submit the address\n"
                + "Escape - Close the page");
    }
}
