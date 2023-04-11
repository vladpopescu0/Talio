package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateTagCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private CreateTagCtrl sut;
    private Board board;

    /**
     * Setup for the test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        User user = new User("User");
        board = new Board(user, "Board");
        sut = new CreateTagCtrl(mainCtrlMock, board, serverUtilsMock);
    }

    /**
     * Test for the constructor
     */
    @Test
    public void constructorTest() {
        assertNotNull(new CreateTagCtrl(mainCtrlMock, board, serverUtilsMock));
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
        Board b1 = new Board(new User("a"), "B");
        sut.setBoard(b1);
        assertEquals(sut.getBoard(), b1);
    }

    /**
     * Test for additionalHelp
     */
    @Test
    public void additionalHelpTest() {
        assertEquals(sut.additionalHelp(), "Create Tag specific shortcuts:\n"
                + "Enter - Create a Tag\n"
                + "Escape - Close the page");
    }
}
