package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ViewAddTagsCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private ViewAddTagsCtrl sut;
    private Board board;
    private Card card;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        User u = new User("u");
        board = new Board(u, "name");
        board.setId(2L);
        card = new Card("card");
        card.setId(5L);

        sut = new ViewAddTagsCtrl(serverUtilsMock, mainCtrlMock, board, card);
    }

    /**
     * Constructor test
     */
    @Test
    public void constructorTest() {
        sut = new ViewAddTagsCtrl(serverUtilsMock, mainCtrlMock, board, card);
        assertNotNull(sut);
    }

    /**
     * Testing the method bound to the back button
     */
    @Test
    public void backTest(){
        sut.back();
        Mockito.verify(mainCtrlMock).closeSecondaryStage();
    }

    /**
     * Testing the getter for Board
     */
    @Test
    public void testGetBoard() {
        Board b = new Board();
        sut = new ViewAddTagsCtrl(serverUtilsMock, mainCtrlMock, b, card);
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

    /**
     * Testing the getter for Card
     */
    @Test
    public void testGetCard() {
        Card c = new Card();
        sut = new ViewAddTagsCtrl(serverUtilsMock, mainCtrlMock, board, c);
        assertEquals(c, sut.getCard());
    }

    /**
     * Testing the setter for Card
     */
    @Test
    public void testSetCard() {
        Card c = new Card();
        c.setId(3L);
        sut.setCard(c);
        assertEquals(sut.getCard(), c);
    }

    /**
     * Testing the getter for Shortcut
     */
    @Test
    public void testGetShortcut() {
        assertFalse(sut.getShortcut());
        sut.setShortcut(true);
        assertTrue(sut.getShortcut());
        sut.setShortcut(false);
        assertFalse(sut.getShortcut());
    }

    /**
     * Testing the setter for Shortcut
     */
    @Test
    public void testSetShortcut() {
        sut.setShortcut(true);
        assertTrue(sut.getShortcut());
    }
}
