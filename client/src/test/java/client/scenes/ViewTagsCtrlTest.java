package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

public class ViewTagsCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private ViewTagsCtrl sut;
    private Board board;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);
        User u = new User("u");
        board = new Board(u, "name");
        board.setId(2L);

        sut = new ViewTagsCtrl(serverUtilsMock, mainCtrlMock, board);

        Mockito.when(serverUtilsMock.getBoardByID(eq(board.getId()))).thenReturn(board);
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new ViewTagsCtrl(serverUtilsMock, mainCtrlMock, board);
        assertNotNull(sut);
    }

    /**
     * Testing the method bound to the back button
     */
    @Test
    public void backTest(){
        sut.back();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
        Mockito.verify(mainCtrlMock).showBoardView(board);
        Mockito.verify(boardViewCtrlMock).refocusFromBackup();
    }

    /**
     * Testing the getter for Board
     */
    @Test
    public void testGetBoard() {
        Board b = new Board();
        sut = new ViewTagsCtrl(serverUtilsMock, mainCtrlMock, b);
        assertEquals(b, sut.getBoard());
    }

    /**
     * Testing the setter for Board
     */
    @Test
    public void testSetBoard() {
        Board b = new Board();
        b.setId(3L);
        sut.setBoard(b);
        assertEquals(sut.getBoard(), b);
    }
}
