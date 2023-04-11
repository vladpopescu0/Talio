package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class CardDetailsCtrlTest {
    private MainCtrl mainCtrlMock;
    private ServerUtils serverUtilsMock;
    private BoardViewCtrl boardViewCtrlMock;
    private CardDetailsViewCtr sut;
    private Card card;
    private Board board;
    private User user;

    /**
     * Setup for the tests
     */
    @BeforeEach
    public void setup() {
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        serverUtilsMock = Mockito.mock(ServerUtils.class);
        boardViewCtrlMock = Mockito.mock(BoardViewCtrl.class);
        user = new User("User");
        card = new Card("Card");
        board = new Board(user, "Board");
        CardList cl = new CardList("List");
        cl.addCard(card);
        board.addList(cl);
        user.setId(1L);
        card.setId(2L);
        board.setId(3L);
        cl.setId(4L);
        sut = new CardDetailsViewCtr(serverUtilsMock, mainCtrlMock, card, board);
        //sut.init();
        Mockito.when(mainCtrlMock.getBoardViewCtrl()).thenReturn(boardViewCtrlMock);
        Mockito.when(boardViewCtrlMock.getBoard()).thenReturn(board);
    }

    /**
     * Test for the constructor
     */
    @Test
    public void ConstructorTest() {
        sut = new CardDetailsViewCtr(serverUtilsMock, mainCtrlMock, card, board);
        assertNotNull(sut);
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
        Board b1 = new Board(user, "new");
        sut.setBoard(b1);
        assertEquals(b1, sut.getBoard());
    }

    /**
     * Test for getCard
     */
    @Test
    public void getCardTest() {
        assertEquals(card, sut.getCard());
    }

    /**
     * Test for setCard
     */
    @Test
    public void setCardTest() {
        Card c1 = new Card("new");
        sut.setCard(c1);
        assertEquals(c1, sut.getCard());
    }

    /**
     * Test for back
     */
    @Test
    public void backTest() {
        sut.back();
        Mockito.verify(mainCtrlMock).showBoardView(board);
    }

    /**
     * Test for addTag
     */
    @Test
    public void addTagTest() {
        sut.addTag();
        Mockito.verify(mainCtrlMock).showViewAddTag(board, card, false);
    }

    /**
     * Test for setUnlocked
     */
    @Test
    public void setUnlockedTest() {
        sut.setUnlocked(true);
        assertTrue(sut.getUnlocked());
    }

}
