package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class JoinBoardByLinkCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private JoinBoardByLinkCtrl sut;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);

        sut = new JoinBoardByLinkCtrl(serverUtilsMock, mainCtrlMock);
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new JoinBoardByLinkCtrl(serverUtilsMock, mainCtrlMock);
        assertNotNull(sut);
    }

    /**
     * Testing the method bound to the cancel button
     */
    @Test
    public void cancelTest() {
        sut.cancel();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * Testing the method returning additional help for that page
     */
    @Test
    public void testAdditionalHelp() {
        assertEquals(sut.additionalHelp(), "Join by Code specific shortcuts:\n"
                + "Enter - Submit the code\n"
                + "Escape - Close the page");
    }
}