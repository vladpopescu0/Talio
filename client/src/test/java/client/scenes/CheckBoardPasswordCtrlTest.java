package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CheckBoardPasswordCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private CheckBoardPasswordViewCtrl sut;
    private Board board;
    private User user;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);
        user = new User("User");
        board = new Board(user, "Board");
        sut = new CheckBoardPasswordViewCtrl(serverUtilsMock, mainCtrlMock, board);
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * Test for constructor
     */
    @Test
    public void constructorTest() {
        assertNotNull(new CheckBoardPasswordViewCtrl(serverUtilsMock,
                mainCtrlMock, board));
    }

    /**
     * Test for getBoard
     */
    @Test
    public void getBoardTest() {
        assertEquals(board, sut.getBoard());
    }

    /**
     * Test for setBoard
     */
    @Test
    public void setBoardTest() {
        Board b1 = new Board(user, "b");
        sut.setBoard(b1);
        assertEquals(sut.getBoard(), b1);
    }

    /**
     * Test for cancel
     */
    @Test
    public void cancelTest() {
        sut.cancel();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * Test for additionalHelp
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(), "Check Board Password specific shortcuts:\n"
                + "Enter - Submit the password\n"
                + "Escape - Close the page");
    }
}
