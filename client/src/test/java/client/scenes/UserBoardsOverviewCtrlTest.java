package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class UserBoardsOverviewCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private UserBoardsOverviewCtrl sut;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);

        sut = new UserBoardsOverviewCtrl(serverUtilsMock, mainCtrlMock);
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new UserBoardsOverviewCtrl(serverUtilsMock, mainCtrlMock);
        assertNotNull(sut);
    }

    /**
     * Testing the method bound to the To All Boards button
     */
    @Test
    public void toAllBoardsTest(){
        sut.toAllBoards();
        Mockito.verify(mainCtrlMock).showOverview();
    }
}
