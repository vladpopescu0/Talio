package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateBoardViewCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardsOverviewCtrl boardsOverviewCtrlMock;
    private CreateBoardViewCtrl sut;
    private User user;

    /**
     * Sets up the tests
     */
    @BeforeEach
    public void setup() {
        user = new User("user");
        user.setId(1L);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardsOverviewCtrlMock = Mockito.mock(BoardsOverviewCtrl.class);
        sut = new CreateBoardViewCtrl(serverUtilsMock, mainCtrlMock);
        mainCtrlMock.setCurrentUser(user);
    }

    /**
     * Test for the constructor
     */
    @Test
    public void ConstructorTest() {
        sut = new CreateBoardViewCtrl(serverUtilsMock, mainCtrlMock);
        assertNotNull(sut);
    }

    /**
     * Test for additionalHelp
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(), "Add Board specific shortcuts:\n"
                + "Enter - Create a board\n"
                + "Escape - Close the page");
    }
}
