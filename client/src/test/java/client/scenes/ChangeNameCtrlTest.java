package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeNameCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private ChangeNameCtrl sut;
    private Board board;

    /**
     * Setup for the test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);
        User user = new User("User");
        board = new Board(user, "Board");
        sut = new ChangeNameCtrl(mainCtrlMock, serverUtilsMock);
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * Test for the constructor
     */
    @Test
    public void constructorTest() {
        sut = new ChangeNameCtrl(mainCtrlMock, serverUtilsMock);
        assertNotNull(sut);
    }

    /**
     * Test for setId
     */
    @Test
    public void setIdTest() {
        sut.setId(1L);
        assertEquals(1L, sut.getId());
    }

    /**
     * Test for getId
     */
    @Test
    public void getIdTest() {
        assertNull(sut.getId());
    }

    /**
     * Test for getBoard
     */
    @Test
    public void getBoardTest() {
        assertNull(sut.getBoard());
    }

    /**
     * Test for setBoard
     */
    @Test
    public void setBoardTest() {
        sut.setBoard(board);
        assertEquals(board, sut.getBoard());
    }

    /**
     * Test for additionalHelp
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(), "Edit List specific shortcuts:\n"
                + "Enter - Submit the name change\n"
                + "Escape - Close the page");
    }
}
