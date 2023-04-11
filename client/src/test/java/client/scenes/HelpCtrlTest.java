package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class HelpCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private HelpCtrl sut;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);

        sut = new HelpCtrl(serverUtilsMock, mainCtrlMock);
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new HelpCtrl(serverUtilsMock, mainCtrlMock);
        assertNotNull(sut);
    }
}