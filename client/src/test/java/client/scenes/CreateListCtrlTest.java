package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class CreateListCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private CreateListCtrl sut;
    private Board board;

    /**
     * Setup for the tests
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);
        User user = new User("User");
        board = new Board(user, "Board");
        sut = new CreateListCtrl(mainCtrlMock, board, serverUtilsMock);
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * Test for Constructor
     */
    @Test
    public void ConstructorTest() {
        sut = new CreateListCtrl(mainCtrlMock, board, serverUtilsMock);
        assertNotNull(sut);
    }

    /**
     * Test of setBoard
     */
    @Test
    public void setBoardTest() {
        Board b1 = new Board(new User("1"), "U");
        sut.setBoard(b1);
        assertEquals(b1, sut.getBoard());
    }

    /**
     * Test for additionalHelp
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(), "Add List specific shortcuts:\n"
                + "Enter - Create a list\n"
                + "Escape - Close the page");
    }
}
